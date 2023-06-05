package dku.cloudcomputing.surveyresultserver.service.surveyresult;

import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceOption;
import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceSurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.survey.SubjectiveSurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SurveyResult;
import dku.cloudcomputing.surveyresultserver.exception.dto.CannotConvertToEntityException;
import dku.cloudcomputing.surveyresultserver.exception.dto.WrongMultipleChoiceException;
import dku.cloudcomputing.surveyresultserver.exception.dto.WrongSurveyDetailException;
import dku.cloudcomputing.surveyresultserver.exception.survey.NoSuchSurveyException;
import dku.cloudcomputing.surveyresultserver.repository.survey.MultipleChoiceOptionRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyDetailRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyRepository;
import dku.cloudcomputing.surveyresultserver.repository.surveyresult.SurveyResultRepository;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.dto.CreateSurveyResultDto;
import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyResultService {
    private final SurveyRepository surveyRepository;
    private final SurveyDetailRepository detailRepository;
    private final MultipleChoiceOptionRepository optionRepository;
    private final SurveyResultRepository resultRepository;

    public void saveSurveyResult(Long surveyId, List<CreateSurveyResultDto> surveyResultDtos) {
        if(!surveyRepository.existsById(surveyId)) throw new NoSuchSurveyException();

        List<SurveyDetail> surveyDetails = detailRepository.findSurveyDetailsBySurveyId(surveyId);
        List<Long> surveyDetailIds = extractIdsFromSurveyDetails(surveyDetails);
        validateSurveyDetailDto(surveyResultDtos, surveyDetails);

        List<MultipleChoiceOption> options = optionRepository.findBySurveyDetailIds(surveyDetailIds);
        validateOptionIdsInDto(surveyResultDtos, options);

        resultRepository.saveAll(convertDtoToSurveyResult(surveyResultDtos, surveyDetails, options));
    }

    private static List<SurveyResult> convertDtoToSurveyResult(List<CreateSurveyResultDto> surveyResultDtos, List<SurveyDetail> surveyDetails, List<MultipleChoiceOption> options) {
        Map<Long, SurveyDetail> mappedSurveyDetails = mapSurveyDetailToId(surveyDetails);
        Map<Long, MultipleChoiceOption> mappedOptions = mapMultipleChoiceOptionToId(options);
        return surveyResultDtos.stream().map(e -> {
            if (e.getDetailType().equals(SurveyDetailType.SUBJECTIVE))
                return e.convertToSubjectiveEntity(mappedSurveyDetails.get(e.getSurveyDetailId()));
            else if (e.getDetailType().equals(SurveyDetailType.MULTIPLE_CHOICE))
                return e.convertToMultipleChoiceEntity(mappedSurveyDetails.get(e.getSurveyDetailId()), mappedOptions.get(e.getOptionId()));
            throw new CannotConvertToEntityException();
        }).toList();
    }

    private static void validateOptionIdsInDto(List<CreateSurveyResultDto> surveyResultDtos, List<MultipleChoiceOption> multipleChoiceOptions) {
        Map<Long, List<MultipleChoiceOption>> mappedOptions =
                multipleChoiceOptions.stream().collect(Collectors.groupingBy(e -> e.getSurveyDetail().getId()));

        surveyResultDtos.stream().filter(e -> e.getDetailType().equals(SurveyDetailType.MULTIPLE_CHOICE))
                .forEach(e -> {
                    List<Long> optionIds = mappedOptions.get(e.getSurveyDetailId()).stream()
                            .map(MultipleChoiceOption::getId).toList();
                    if(!optionIds.contains(e.getOptionId())) throw new WrongMultipleChoiceException();
                });
    }

    private static void validateSurveyDetailDto(List<CreateSurveyResultDto> surveyResultDtos, List<SurveyDetail> surveyDetails) {
        if(surveyResultDtos.size() != surveyDetails.size()) throw new WrongSurveyDetailException();

        Map<Long, List<CreateSurveyResultDto>> mappedResultDto =
                surveyResultDtos.stream().collect(Collectors.groupingBy(CreateSurveyResultDto::getSurveyDetailId));

        surveyDetails.forEach(e -> {
            List<CreateSurveyResultDto> resultDtos = mappedResultDto.get(e.getId());
            if(resultDtos == null || resultDtos.size() != 1) throw new WrongSurveyDetailException();

            CreateSurveyResultDto resultDto = resultDtos.get(0);
            if(e instanceof SubjectiveSurveyDetail && !resultDto.getDetailType().equals(SurveyDetailType.SUBJECTIVE) ||
                    e instanceof MultipleChoiceSurveyDetail && !resultDto.getDetailType().equals(SurveyDetailType.MULTIPLE_CHOICE))
                throw new WrongSurveyDetailException();
        });
    }

    private static Map<Long, MultipleChoiceOption> mapMultipleChoiceOptionToId(List<MultipleChoiceOption> options) {
        Map<Long, MultipleChoiceOption> mappedOptions = new HashMap<>();
        options.forEach(e -> mappedOptions.put(e.getId(), e));
        return mappedOptions;
    }

    private static Map<Long, SurveyDetail> mapSurveyDetailToId(List<SurveyDetail> surveyDetails) {
        Map<Long, SurveyDetail> mappedSurveyDetails = new HashMap<>();
        surveyDetails.forEach(e -> mappedSurveyDetails.put(e.getId(), e));
        return mappedSurveyDetails;
    }

    private static List<Long> extractIdsFromSurveyDetails(List<SurveyDetail> surveyDetails) {
        return surveyDetails.stream().map(SurveyDetail::getId).toList();
    }
}