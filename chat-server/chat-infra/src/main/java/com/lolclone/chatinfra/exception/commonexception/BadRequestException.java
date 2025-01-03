package com.lolclone.chatinfra.exception.commonexception;

import com.lolclone.chatinfra.exception.domain.ExceptionType;

public class BadRequestException extends ChatException {
    public BadRequestException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}