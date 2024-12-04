package com.lolclone.chat_server.exception.common;

import com.lolclone.chat_server.exception.domain.ExceptionType;

public class ValidException extends ChatException {
    public ValidException(ExceptionType exceptionType) {
        super(exceptionType);
    }
} 