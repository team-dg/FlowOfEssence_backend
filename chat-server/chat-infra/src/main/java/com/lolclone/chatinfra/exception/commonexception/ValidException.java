package com.lolclone.chatinfra.exception.commonexception;

import com.lolclone.chatinfra.exception.domain.ExceptionType;

public class ValidException extends ChatException {
    public ValidException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}