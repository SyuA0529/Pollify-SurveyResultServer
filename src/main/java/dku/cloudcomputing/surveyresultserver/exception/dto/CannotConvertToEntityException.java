package dku.cloudcomputing.surveyresultserver.exception.dto;

import dku.cloudcomputing.surveyresultserver.exception.ClientOccurException;

public class CannotConvertToEntityException extends ClientOccurException {
    public CannotConvertToEntityException() {
        new CannotConvertToEntityException("엔티티로 변환할 수 없습니다");
    }

    public CannotConvertToEntityException(String message) {
        super(message);
    }
}
