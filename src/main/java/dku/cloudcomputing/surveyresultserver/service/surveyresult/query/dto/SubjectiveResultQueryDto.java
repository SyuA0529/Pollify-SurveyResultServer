package dku.cloudcomputing.surveyresultserver.service.surveyresult.query.dto;

import lombok.Getter;

@Getter
public class SubjectiveResultQueryDto extends SurveyResultQueryDto {
    private String content;

    public SubjectiveResultQueryDto(Long surveyDetailId, String content) {
        super(surveyDetailId);
        this.content = content;
    }
}
