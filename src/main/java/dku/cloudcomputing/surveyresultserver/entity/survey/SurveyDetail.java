package dku.cloudcomputing.surveyresultserver.entity.survey;

import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SurveyResult;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "survey_type")
@NoArgsConstructor
public abstract class SurveyDetail {
    @Id
    private Long id;

    @Column(nullable = false)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "surveyDetail")
    private final List<SurveyResult> surveyResults = new ArrayList<>();

    public SurveyDetail(Long id, Survey survey, String question) {
        this.id = id;
        this.survey = survey;
        this.question = question;

        survey.getSurveyDetails().add(this);
    }
}
