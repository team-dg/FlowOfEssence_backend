package com.lolclone.common_module.exception.dto;

import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.lolclone.common_module.exception.domain.ExceptionType;

import static java.util.stream.Collectors.toMap;

public record ValidErrorResponse(
    ExceptionType exceptionType,
    String message,
    Map<String, String> result
) {
    public static ValidErrorResponse from(MethodArgumentNotValidException e) {
        Map<String, String> result = e.getBindingResult().getFieldErrors().stream()
            .collect(toMap(FieldError::getField, ValidErrorResponse::getFieldErrorMessage));
        return new ValidErrorResponse(
            ExceptionType.INVALID_REQUEST_ARGUMENT,
            ExceptionType.INVALID_REQUEST_ARGUMENT.getMessage(),
            result
        );
    }

    private static String getFieldErrorMessage(FieldError error) {
        String message = error.getDefaultMessage();
        if (message == null) {
            return "잘못된 요청입니다.";
        }
        return message;
    }
}
