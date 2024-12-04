package com.lolclone.chat_server.exception.common;

import com.lolclone.chat_server.exception.domain.ExceptionType;

public class BadRequestException extends ChatException {
    public BadRequestException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}