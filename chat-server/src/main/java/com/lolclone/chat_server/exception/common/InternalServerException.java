package com.lolclone.chat_server.exception.common;

import com.lolclone.chat_server.exception.domain.ExceptionType;

public class InternalServerException extends ChatException {
    public InternalServerException(ExceptionType exceptionType) {
        super(exceptionType);
    }
} 