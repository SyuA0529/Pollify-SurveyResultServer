package dku.cloudcomputing.surveyresultserver.service.survey.dto;

import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceSurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.survey.SubjectiveSurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.survey.Survey;
import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import dku.cloudcomputing.surveyresultserver.exception.dto.CannotConvertToEntityException;
import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class KafkaSurveyDetailDto {
    private Long id;
    private String question;
    private SurveyDetailType surveyDetailType;
    private final List<KafkaDetailOptionDto> options = new ArrayList<>();

    public SurveyDetail convertToEntity(Survey survey) {
        if(surveyDetailType == SurveyDetailType.SUBJECTIVE)
            return new SubjectiveSurveyDetail(id, survey, question);
        else if(surveyDetailType == SurveyDetailType.MULTIPLE_CHOICE) {
            MultipleChoiceSurveyDetail multipleChoiceSurveyDetail = new MultipleChoiceSurveyDetail(id, survey, question);
            options.forEach(e -> e.convertToEntity(multipleChoiceSurveyDetail));
            return multipleChoiceSurveyDetail;
        }
        throw new CannotConvertToEntityException();
    }
}
