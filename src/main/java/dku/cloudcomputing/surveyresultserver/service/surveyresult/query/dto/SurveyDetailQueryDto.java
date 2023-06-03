package dku.cloudcomputing.surveyresultserver.service.surveyresult.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class SurveyDetailQueryDto {
    @JsonIgnore
    private Long id;
    private String question;
    private SurveyDetailType detailType;
    private int resultCount;
    private List<SurveyResultQueryDto> results = new ArrayList<>();

    public SurveyDetailQueryDto(Long id, String question, SurveyDetailType detailType) {
        this.id = id;
        this.question = question;
        this.detailType = detailType;
    }
}
