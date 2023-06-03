package dku.cloudcomputing.surveyresultserver.entity.surveyresult;

import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SUBJECTIVE")
@Getter
@NoArgsConstructor
public class SubjectiveSurveyResult extends SurveyResult {
    private String content;

    public SubjectiveSurveyResult(SurveyDetail surveyDetail, String content) {
        super(surveyDetail);
        this.content = content;
    }
}
