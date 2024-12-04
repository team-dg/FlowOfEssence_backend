package com.lolclone.api_gateway_server.common.exception.dto;

import com.lolclone.api_gateway_server.common.exception.commonexception.ApiGateWayException;
import com.lolclone.api_gateway_server.common.exception.domain.ExceptionType;

public record ExceptionResponse(
    String code,
    String message
) {
    public static ExceptionResponse from(ApiGateWayException authenticationException) {
        return ExceptionResponse.from(authenticationException.getExceptionType());
    }

    public static ExceptionResponse from(ExceptionType exceptionType) {
        return new ExceptionResponse(exceptionType.getCode(), exceptionType.getMessage());
    }
}
