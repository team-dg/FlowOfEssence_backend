package com.lolclone.common_module.exception.dto;

import com.lolclone.common_module.commonexception.FlowOfEssenceException;
import com.lolclone.common_module.exception.domain.ExceptionType;

public record ExceptionResponse(
    String code,
    String message
) {
    public static ExceptionResponse from(FlowOfEssenceException flowOfEssenceException) {
        return ExceptionResponse.from(flowOfEssenceException.getExceptionType());
    }

    public static ExceptionResponse from(ExceptionType exceptionType) {
        return new ExceptionResponse(exceptionType.getCode(), exceptionType.getMessage());
    }
}
