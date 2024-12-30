package com.lolclone.userinfra.exception.dto;

import com.lolclone.userinfra.exception.commonexception.UserException;
import com.lolclone.userinfra.exception.domain.ExceptionType;

public record ExceptionResponse(
    String code,
    String message
) {
    public static ExceptionResponse from(UserException userException) {
        return ExceptionResponse.from(userException.getExceptionType());
    }

    public static ExceptionResponse from(ExceptionType exceptionType) {
        return new ExceptionResponse(exceptionType.getCode(), exceptionType.getMessage());
    }
}
