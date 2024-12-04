package com.lolclone.authentication_management_server.common.exception.dto;

import com.lolclone.authentication_management_server.common.commonexception.AuthenticationException;
import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;

public record ExceptionResponse(
    String code,
    String message
) {
    public static ExceptionResponse from(AuthenticationException authenticationException) {
        return ExceptionResponse.from(authenticationException.getExceptionType());
    }

    public static ExceptionResponse from(ExceptionType exceptionType) {
        return new ExceptionResponse(exceptionType.getCode(), exceptionType.getMessage());
    }
}
