package dku.cloudcomputing.surveyresultserver.service.surveyresult.query;

import dku.cloudcomputing.surveyresultserver.entity.member.Member;
import dku.cloudcomputing.surveyresultserver.entity.survey.Survey;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.MultipleChoiceSurveyResult;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SubjectiveSurveyResult;
import dku.cloudcomputing.surveyresultserver.exception.member.NoSuchMemberException;
import dku.cloudcomputing.surveyresultserver.exception.member.NotMatchMemberException;
import dku.cloudcomputing.surveyresultserver.exception.survey.NoSuchSurveyException;
import dku.cloudcomputing.surveyresultserver.repository.member.MemberRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.query.MultipleChoiceOptionQueryRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.query.SurveyQueryRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.query.dto.MultipleChoiceOptionQueryDto;
import dku.cloudcomputing.surveyresultserver.security.JwtAuthenticator;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.query.dto.*;
import dku.cloudcomputing.surveyresultserver.repository.surveyresult.query.SurveyResultQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SurveyResultQueryService {
    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyQueryRepository surveyQueryRepository;
    private final MultipleChoiceOptionQueryRepository optionQueryRepository;
    private final SurveyResultQueryRepository resultQueryRepository;

    private final JwtAuthenticator jwtAuthenticator;

    public SurveyQueryDto querySurveyResult(String token, Long surveyId) {
        validateReqMemberEqToCreateMember(token, surveyId);

        SurveyQueryDto surveyQueryDto = new SurveyQueryDto(
                surveyQueryRepository.findDetailSurveyById(surveyId)
                        .orElseThrow(NoSuchSurveyException::new));

        Map<Long, List<MultipleChoiceOptionQueryDto>> mappedOptionQueryDtos =
                getOptionQueryDtosMappedByDetailId(surveyQueryDto);

        surveyQueryDto.getSurveyDetails().stream()
                .filter(o -> o instanceof MultipleChoiceSurveyDetailQueryDto)
                .forEach(o -> ((MultipleChoiceSurveyDetailQueryDto) o)
                        .setOptions(mappedOptionQueryDtos.get(o.getId())));

        Map<Long, List<SurveyResultQueryDto>> mappedSurveyResultDtos = getSurveyResultDtosMappedByDetailId(surveyQueryDto);


        surveyQueryDto.getSurveyDetails()
                .forEach(e -> {
                    List<SurveyResultQueryDto> resultQueryDtos = mappedSurveyResultDtos.get(e.getId());
                    if(resultQueryDtos == null || resultQueryDtos.isEmpty()) return;
                    e.setResults(resultQueryDtos);
                    e.setResultCount(resultQueryDtos.size());
                });

        return surveyQueryDto;
    }

    private Map<Long, List<SurveyResultQueryDto>> getSurveyResultDtosMappedByDetailId(SurveyQueryDto surveyQueryDto) {
        return resultQueryRepository.findByDetailIdList(
                surveyQueryDto.getSurveyDetails().stream().map(SurveyDetailQueryDto::getId).toList()
        ).stream().map(e -> {
            if (e instanceof SubjectiveSurveyResult)
                return new SubjectiveResultQueryDto(e.getSurveyDetail().getId(), ((SubjectiveSurveyResult) e).getContent());
            else if (e instanceof MultipleChoiceSurveyResult)
                return new MultipleChoiceResultQueryDto(e.getSurveyDetail().getId(), ((MultipleChoiceSurveyResult) e).getOption().getId());
            throw new RuntimeException();
        }).collect(Collectors.groupingBy(SurveyResultQueryDto::getSurveyDetailId));
    }

    private Map<Long, List<MultipleChoiceOptionQueryDto>> getOptionQueryDtosMappedByDetailId(SurveyQueryDto surveyQueryDto) {
        return optionQueryRepository.findBySurveyDetailIds(getMultipleChoiceIds(surveyQueryDto)).stream()
                .collect(Collectors.groupingBy(MultipleChoiceOptionQueryDto::getSurveyDetailId));
    }

    private static List<Long> getMultipleChoiceIds(SurveyQueryDto surveyQueryDto) {
        return surveyQueryDto.getSurveyDetails().stream()
                .filter(o -> o instanceof MultipleChoiceSurveyDetailQueryDto)
                .map(SurveyDetailQueryDto::getId).collect(Collectors.toList());
    }

    private void validateReqMemberEqToCreateMember(String token, Long surveyId) {
        Member requestMember = memberRepository.findByEmail(jwtAuthenticator.getEmail(token))
                .orElseThrow(NoSuchMemberException::new);

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(NoSuchSurveyException::new);

        if(!survey.getMember().equals(requestMember))
            throw new NotMatchMemberException();
    }
}
