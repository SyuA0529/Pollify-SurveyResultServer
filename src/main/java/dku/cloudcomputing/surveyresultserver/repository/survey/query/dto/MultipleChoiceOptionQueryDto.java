package dku.cloudcomputing.surveyresultserver.repository.survey.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class MultipleChoiceOptionQueryDto {
    @JsonIgnore
    private final Long surveyDetailId;
    private final Long optionId;
    private final String option;

    public MultipleChoiceOptionQueryDto(Long surveyDetailId, Long optionId, String option) {
        this.surveyDetailId = surveyDetailId;
        this.optionId = optionId;
        this.option = option;
    }
}
