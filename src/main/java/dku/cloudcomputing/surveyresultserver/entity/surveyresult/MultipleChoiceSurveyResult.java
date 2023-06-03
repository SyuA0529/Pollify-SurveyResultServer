package dku.cloudcomputing.surveyresultserver.entity.surveyresult;

import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceOption;
import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
@Getter
@NoArgsConstructor
public class MultipleChoiceSurveyResult extends SurveyResult{
    @ManyToOne(fetch = FetchType.LAZY)
    private MultipleChoiceOption option;

    public MultipleChoiceSurveyResult(SurveyDetail surveyDetail, MultipleChoiceOption option) {
        super(surveyDetail);
        this.option = option;
    }
}
