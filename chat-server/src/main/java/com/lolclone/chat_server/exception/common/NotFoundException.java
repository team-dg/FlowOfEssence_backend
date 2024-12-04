package com.lolclone.chat_server.exception.common;

import com.lolclone.chat_server.exception.domain.ExceptionType;

public class NotFoundException extends ChatException {
    public NotFoundException(ExceptionType exceptionType) {
        super(exceptionType);
    }
} 