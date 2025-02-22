package com.lolclone.chatinfra.exception.commonexception;

import com.lolclone.chatinfra.exception.domain.ExceptionType;

public class UnexpectedException extends ChatException {
    public UnexpectedException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}