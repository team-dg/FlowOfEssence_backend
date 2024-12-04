package com.lolclone.chat_server.exception.common;

import com.lolclone.chat_server.exception.domain.ExceptionType;

public class ForbiddenException extends ChatException {
    public ForbiddenException(ExceptionType exceptionType) {
        super(exceptionType);
    }
} 