package dku.cloudcomputing.surveyresultserver.exception.dto;

import dku.cloudcomputing.surveyresultserver.exception.ClientOccurException;

public class WrongMultipleChoiceIdException extends ClientOccurException {
    public WrongMultipleChoiceIdException() {
    }

    public WrongMultipleChoiceIdException(String message) {
        super(message);
    }
}
