package dku.cloudcomputing.surveyresultserver.service.surveyresult;

import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceOption;
import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SurveyResult;
import dku.cloudcomputing.surveyresultserver.exception.dto.CannotConvertToEntityException;
import dku.cloudcomputing.surveyresultserver.exception.dto.WrongMultipleChoiceIdException;
import dku.cloudcomputing.surveyresultserver.exception.dto.WrongSurveyDetailIdException;
import dku.cloudcomputing.surveyresultserver.repository.survey.MultipleChoiceOptionRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyDetailRepository;
import dku.cloudcomputing.surveyresultserver.repository.surveyresult.SurveyResultRepository;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.dto.CreateSurveyResultDto;
import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyResultService {
    private final SurveyDetailRepository detailRepository;
    private final MultipleChoiceOptionRepository optionRepository;
    private final SurveyResultRepository resultRepository;

    public void saveSurveyResult(Long surveyId, List<CreateSurveyResultDto> surveyResultDtos) {
        List<SurveyDetail> surveyDetails = detailRepository.findSurveyDetailsBySurveyId(surveyId);
        List<Long> surveyDetailIds = extractIdsFromSurveyDetails(surveyDetails);
        validateSurveyDetailIdsInDto(surveyResultDtos, surveyDetailIds);

        List<MultipleChoiceOption> options = optionRepository.findBySurveyDetailIds(surveyDetailIds);
        validateOptionIdsInDto(surveyResultDtos, options);

        resultRepository.saveAll(convertDtoToSuveyResult(surveyResultDtos, surveyDetails, options));
    }

    private static List<SurveyResult> convertDtoToSuveyResult(List<CreateSurveyResultDto> surveyResultDtos, List<SurveyDetail> surveyDetails, List<MultipleChoiceOption> options) {
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

    private static void validateOptionIdsInDto(List<CreateSurveyResultDto> surveyResultDtos, List<MultipleChoiceOption> options) {
        if(!extractIdsFromMultipleChoiceOptions(options).containsAll(surveyResultDtos.stream()
                .filter(e -> e.getDetailType().equals(SurveyDetailType.MULTIPLE_CHOICE))
                .map(CreateSurveyResultDto::getOptionId).toList()))
            throw new WrongMultipleChoiceIdException();
    }

    private static void validateSurveyDetailIdsInDto(List<CreateSurveyResultDto> surveyResultDtos, List<Long> surveyDetailIds) {
        if(!surveyDetailIds.containsAll(surveyResultDtos.stream()
                .map(CreateSurveyResultDto::getSurveyDetailId).toList()))
            throw new WrongSurveyDetailIdException();
    }

    private static Map<Long, MultipleChoiceOption> mapMultipleChoiceOptionToId(List<MultipleChoiceOption> options) {
        Map<Long, MultipleChoiceOption> mappedOptions = new HashMap<>();
        options.stream().forEach(e -> mappedOptions.put(e.getId(), e));
        return mappedOptions;
    }

    private static Map<Long, SurveyDetail> mapSurveyDetailToId(List<SurveyDetail> surveyDetails) {
        Map<Long, SurveyDetail> mappedSurveyDetails = new HashMap<>();
        surveyDetails.stream().forEach(e -> mappedSurveyDetails.put(e.getId(), e));
        return mappedSurveyDetails;
    }

    private static List<Long> extractIdsFromSurveyDetails(List<SurveyDetail> surveyDetails) {
        return surveyDetails.stream().map(SurveyDetail::getId).toList();
    }

    private static List<Long> extractIdsFromMultipleChoiceOptions(List<MultipleChoiceOption> options) {
        return options.stream().map(MultipleChoiceOption::getId).toList();
    }
}
