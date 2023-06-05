package dku.cloudcomputing.surveyresultserver.service.surveyresult.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class JoinSurveyDto {
    private final List<CreateSurveyResultDto> surveyResults = new ArrayList<>();
}
