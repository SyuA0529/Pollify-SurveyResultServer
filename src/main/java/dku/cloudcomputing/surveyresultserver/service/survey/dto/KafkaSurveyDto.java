package dku.cloudcomputing.surveyresultserver.service.survey.dto;

import dku.cloudcomputing.surveyresultserver.entity.member.Member;
import dku.cloudcomputing.surveyresultserver.entity.survey.Survey;
import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class KafkaSurveyDto {
    private Long id;
    private Long memberId;
    private String name;
    private LocalDate startDate;
    private int duration;
    private boolean visibility;
    private List<KafkaSurveyDetailDto> surveyDetails = new ArrayList<>();

    public Survey convertToSurveyEntity(Member member) {
        Survey survey = new Survey(id, member, name, startDate, duration, visibility);
        surveyDetails.stream().map(e -> e.convertToEntity(survey)).toList();
        return survey;
    }
}
