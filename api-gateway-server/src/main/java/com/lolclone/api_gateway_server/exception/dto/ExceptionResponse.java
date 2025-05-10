package com.lolclone.api_gateway_server.exception.dto;

import com.lolclone.api_gateway_server.exception.commonexception.ApiGatewayException;
import com.lolclone.api_gateway_server.exception.domain.ExceptionType;

public record ExceptionResponse(
    String code,
    String message
) {
    public static ExceptionResponse from(ApiGatewayException apiGatewayException) {
        return ExceptionResponse.from(apiGatewayException.getExceptionType());
    }

    public static ExceptionResponse from(ExceptionType exceptionType) {
        return new ExceptionResponse(exceptionType.getCode(), exceptionType.getMessage());
    }
}
