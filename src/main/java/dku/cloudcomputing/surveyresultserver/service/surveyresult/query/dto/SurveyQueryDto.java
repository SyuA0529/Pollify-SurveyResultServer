package dku.cloudcomputing.surveyresultserver.service.surveyresult.query.dto;

import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceSurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.survey.SubjectiveSurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.survey.Survey;
import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SurveyQueryDto {
    private String name;
    private List<SurveyDetailQueryDto> surveyDetails = new ArrayList<>();

    public SurveyQueryDto(Survey survey) {
        this.name = survey.getName();
        List<SurveyDetail> surveyDetails = survey.getSurveyDetails();
        addSurveyDetailQueryDtos(surveyDetails);
    }

    private void addSurveyDetailQueryDtos(List<SurveyDetail> surveyDetails) {
        for (SurveyDetail surveyDetail : surveyDetails) {
            if(surveyDetail instanceof SubjectiveSurveyDetail)
                this.surveyDetails.add(new SubjectiveSurveyDetailQueryDto(
                        surveyDetail.getId(), surveyDetail.getQuestion(), SurveyDetailType.SUBJECTIVE));
            else if(surveyDetail instanceof MultipleChoiceSurveyDetail)
                this.surveyDetails.add(new MultipleChoiceSurveyDetailQueryDto(
                        surveyDetail.getId(), surveyDetail.getQuestion(), SurveyDetailType.MULTIPLE_CHOICE));
        }
    }
}
