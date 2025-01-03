package com.lolclone.chatinfra.exception.commonexception;

import com.lolclone.chatinfra.exception.domain.ExceptionType;

public class ForbiddenException extends ChatException {
    public ForbiddenException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}