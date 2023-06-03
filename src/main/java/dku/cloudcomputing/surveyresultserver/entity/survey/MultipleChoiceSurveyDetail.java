package dku.cloudcomputing.surveyresultserver.entity.survey;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
@NoArgsConstructor
@Getter
public class MultipleChoiceSurveyDetail extends SurveyDetail{
    @OneToMany(mappedBy = "surveyDetail")
    private List<MultipleChoiceOption> multipleChoiceOptions = new ArrayList<>();

    public MultipleChoiceSurveyDetail(Long id, Survey survey, String question) {
        super(id, survey, question);
    }

    public void clearMultipleChoiceOptions() {
        multipleChoiceOptions.clear();
    }
}
