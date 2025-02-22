package com.lolclone.chatinfra.exception.commonexception;

import com.lolclone.chatinfra.exception.domain.ExceptionType;

public class NotFoundException extends ChatException {
    public NotFoundException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}