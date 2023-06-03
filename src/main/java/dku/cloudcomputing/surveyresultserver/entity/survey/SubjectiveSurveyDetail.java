package dku.cloudcomputing.surveyresultserver.entity.survey;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SUBJECTIVE")
@NoArgsConstructor
public class SubjectiveSurveyDetail extends SurveyDetail{
    public SubjectiveSurveyDetail(Long id, Survey survey, String question) {
        super(id, survey, question);
    }
}
