package dku.cloudcomputing.surveyresultserver.exception.dto;

import dku.cloudcomputing.surveyresultserver.exception.ClientOccurException;

public class WrongSurveyDetailIdException extends ClientOccurException {
    public WrongSurveyDetailIdException() {
    }

    public WrongSurveyDetailIdException(String message) {
        super(message);
    }
}
