package dku.cloudcomputing.surveyresultserver.service.surveyresult.query.dto;

import lombok.Getter;

@Getter
public class MultipleChoiceResultQueryDto extends SurveyResultQueryDto {
    private final Long selectOptionId;

    public MultipleChoiceResultQueryDto(Long surveyDetailId, Long selectOptionId) {
        super(surveyDetailId);
        this.selectOptionId = selectOptionId;
    }
}
