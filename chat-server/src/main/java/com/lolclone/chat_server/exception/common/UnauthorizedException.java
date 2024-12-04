package com.lolclone.chat_server.exception.common;

import com.lolclone.chat_server.exception.domain.ExceptionType;

public class UnauthorizedException extends ChatException {
    public UnauthorizedException(ExceptionType exceptionType) {
        super(exceptionType);
    }
} 