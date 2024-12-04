package com.lolclone.chat_server.exception.common;

import com.lolclone.chat_server.exception.domain.ExceptionType;

public class UnexpectedException extends ChatException {
    public UnexpectedException(ExceptionType exceptionType) {
        super(exceptionType);
    }
} 