package dku.cloudcomputing.surveyresultserver.entity.surveyresult;

import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "resultType")
public abstract class SurveyResult {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SurveyDetail surveyDetail;

    public SurveyResult(SurveyDetail surveyDetail) {
        this.surveyDetail = surveyDetail;
    }
}
