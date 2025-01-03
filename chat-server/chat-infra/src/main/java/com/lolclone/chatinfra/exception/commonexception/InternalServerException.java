package com.lolclone.chatinfra.exception.commonexception;

import com.lolclone.chatinfra.exception.domain.ExceptionType;

public class InternalServerException extends ChatException {
    public InternalServerException(ExceptionType exceptionType) {
        super(exceptionType);
    }
} 