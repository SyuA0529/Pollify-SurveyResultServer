package dku.cloudcomputing.surveyresultserver.service.surveyresult.dto;

import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceOption;
import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.MultipleChoiceSurveyResult;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SubjectiveSurveyResult;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SurveyResult;
import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSurveyResultDto {
    @NotNull
    @Range(min = 1L)
    private Long surveyDetailId;
    @NotNull
    private SurveyDetailType detailType;
    private String content;
    private Long optionId;

    public SurveyResult convertToSubjectiveEntity(SurveyDetail surveyDetail) {
        return new SubjectiveSurveyResult(surveyDetail, content);
    }

    public SurveyResult convertToMultipleChoiceEntity(SurveyDetail surveyDetail, MultipleChoiceOption option) {
        return new MultipleChoiceSurveyResult(surveyDetail, option);
    }
}
