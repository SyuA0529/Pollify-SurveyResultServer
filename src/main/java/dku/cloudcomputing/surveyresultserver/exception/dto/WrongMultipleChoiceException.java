package dku.cloudcomputing.surveyresultserver.exception.dto;

import dku.cloudcomputing.surveyresultserver.exception.ClientOccurException;

public class WrongMultipleChoiceException extends ClientOccurException {
    public WrongMultipleChoiceException() {
    }

    public WrongMultipleChoiceException(String message) {
        super(message);
    }
}
