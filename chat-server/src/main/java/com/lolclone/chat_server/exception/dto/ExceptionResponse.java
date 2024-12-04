package com.lolclone.chat_server.exception.dto;

import com.lolclone.chat_server.exception.common.ChatException;
import com.lolclone.chat_server.exception.domain.ExceptionType;

public record ExceptionResponse(
    String code,
    String message
) {
    public static ExceptionResponse from(ChatException exception) {
        return ExceptionResponse.from(exception.getExceptionType());
    }

    public static ExceptionResponse from(ExceptionType exceptionType) {
        return new ExceptionResponse(exceptionType.getCode(), exceptionType.getMessage());
    }
} 