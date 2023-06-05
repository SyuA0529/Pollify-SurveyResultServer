package dku.cloudcomputing.surveyresultserver.controller.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BindingErrorResponseDto {
    private final String status;
    private final List<FieldBindingErrorDto> errors = new ArrayList<>();

    public BindingErrorResponseDto(String status) {
        this.status = status;
    }
}