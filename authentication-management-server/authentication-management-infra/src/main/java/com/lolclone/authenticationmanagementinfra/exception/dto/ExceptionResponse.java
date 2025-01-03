package com.lolclone.authenticationmanagementinfra.exception.dto;

import com.lolclone.authenticationmanagementinfra.exception.commonexception.AuthenticationException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

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
