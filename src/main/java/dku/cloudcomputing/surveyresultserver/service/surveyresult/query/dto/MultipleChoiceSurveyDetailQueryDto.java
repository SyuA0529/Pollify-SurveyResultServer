package dku.cloudcomputing.surveyresultserver.service.surveyresult.query.dto;

import dku.cloudcomputing.surveyresultserver.repository.survey.query.dto.MultipleChoiceOptionQueryDto;
import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultipleChoiceSurveyDetailQueryDto extends SurveyDetailQueryDto{
    private List<MultipleChoiceOptionQueryDto> options;


    public MultipleChoiceSurveyDetailQueryDto(Long id, String question, SurveyDetailType detailType) {
        super(id, question, detailType);
    }
}
