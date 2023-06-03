package dku.cloudcomputing.surveyresultserver.entity.survey;

import dku.cloudcomputing.surveyresultserver.entity.surveyresult.MultipleChoiceSurveyResult;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class MultipleChoiceOption {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveydetail_id")
    private SurveyDetail surveyDetail;

    @Column(nullable = false)
    private String option;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "option")
    private List<MultipleChoiceSurveyResult> surveyResult;

    public MultipleChoiceOption(Long id, SurveyDetail surveyDetail, String option) {
        this.id = id;
        this.surveyDetail = surveyDetail;
        this.option = option;

        ((MultipleChoiceSurveyDetail) surveyDetail).getMultipleChoiceOptions().add(this);
    }
}
