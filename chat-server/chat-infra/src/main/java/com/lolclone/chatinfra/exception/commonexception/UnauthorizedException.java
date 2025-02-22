package com.lolclone.chatinfra.exception.commonexception;

import com.lolclone.chatinfra.exception.domain.ExceptionType;

public class UnauthorizedException extends ChatException {
    public UnauthorizedException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}