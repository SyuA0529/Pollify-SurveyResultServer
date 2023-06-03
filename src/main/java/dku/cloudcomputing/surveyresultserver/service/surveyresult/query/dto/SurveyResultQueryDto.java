package dku.cloudcomputing.surveyresultserver.service.surveyresult.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class SurveyResultQueryDto {
    @JsonIgnore
    private Long surveyDetailId;
}
