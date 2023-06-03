package dku.cloudcomputing.surveyresultserver.service.survey.dto;

import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceOption;
import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KafkaDetailOptionDto {
    private Long id;
    private String option;

    public MultipleChoiceOption convertToEntity(SurveyDetail surveyDetail) {
        return new MultipleChoiceOption(id, surveyDetail, option);
    }
}
