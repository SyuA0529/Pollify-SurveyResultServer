package dku.cloudcomputing.surveyresultserver.controller;

import dku.cloudcomputing.surveyresultserver.controller.dto.StatusResponseDto;
import dku.cloudcomputing.surveyresultserver.exception.ClientOccurException;
import dku.cloudcomputing.surveyresultserver.exception.dto.FieldBindException;
import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.SurveyResultService;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.dto.JoinSurveyDto;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.query.SurveyResultQueryService;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.query.dto.SurveyQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SurveyResultController {
    private final SurveyResultService resultService;
    private final SurveyResultQueryService resultQueryService;

    @GetMapping("/surveys/{surveyId}/results")
    public SurveyQueryDto querySurveyResult(@RequestHeader(value = "Authorization") String token,
                                            @PathVariable Long surveyId) {
        return resultQueryService.querySurveyResult(getParseableToken(token), surveyId);
    }

    @PostMapping("/surveys/{surveyId}/results")
    public StatusResponseDto joinSurvey(@PathVariable Long surveyId,
                                        @Validated @RequestBody JoinSurveyDto joinSurveyDto,
                                        BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new FieldBindException(bindingResult.getFieldErrors());
        if(
                joinSurveyDto.getSurveyResults().stream()
                .anyMatch(e -> (e.getSurveyDetailId() <= 0) ||
                        (e.getDetailType().equals(SurveyDetailType.SUBJECTIVE) && e.getContent().isEmpty() ||
                        (e.getDetailType().equals(SurveyDetailType.MULTIPLE_CHOICE) && e.getOptionId() <= 0))))
            throw new ClientOccurException();

        resultService.saveSurveyResult(surveyId, joinSurveyDto.getSurveyResults());
        return new StatusResponseDto("success");
    }

    private static String getParseableToken(String token) {
        return token.split(" ")[1].trim();
    }
}
