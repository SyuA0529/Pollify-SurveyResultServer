package dku.cloudcomputing.surveyresultserver.exception.dto;

import dku.cloudcomputing.surveyresultserver.exception.ClientOccurException;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class FieldBindException extends ClientOccurException {
    List<FieldError> fieldErrors;

    public FieldBindException(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
