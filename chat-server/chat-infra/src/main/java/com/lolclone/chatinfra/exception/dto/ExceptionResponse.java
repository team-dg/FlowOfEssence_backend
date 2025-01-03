package com.lolclone.chatinfra.exception.dto;

import com.lolclone.chatinfra.exception.commonexception.ChatException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;

public record ExceptionResponse(
    String code,
    String message
) {
    public static ExceptionResponse from(ChatException chatException) {
        return ExceptionResponse.from(chatException.getExceptionType());
    }

    public static ExceptionResponse from(ExceptionType exceptionType) {
        return new ExceptionResponse(exceptionType.getCode(), exceptionType.getMessage());
    }
}
