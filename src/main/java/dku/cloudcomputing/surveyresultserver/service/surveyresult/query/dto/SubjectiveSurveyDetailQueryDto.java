package dku.cloudcomputing.surveyresultserver.service.surveyresult.query.dto;

import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import lombok.Getter;

@Getter
public class SubjectiveSurveyDetailQueryDto extends SurveyDetailQueryDto{
    public SubjectiveSurveyDetailQueryDto(Long id, String question, SurveyDetailType detailType) {
        super(id, question, detailType);
    }
}
