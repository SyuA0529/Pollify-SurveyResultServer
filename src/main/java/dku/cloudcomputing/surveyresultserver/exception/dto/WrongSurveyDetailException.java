package dku.cloudcomputing.surveyresultserver.exception.dto;

import dku.cloudcomputing.surveyresultserver.exception.ClientOccurException;

public class WrongSurveyDetailException extends ClientOccurException {
    public WrongSurveyDetailException() {
    }

    public WrongSurveyDetailException(String message) {
        super(message);
    }
}
