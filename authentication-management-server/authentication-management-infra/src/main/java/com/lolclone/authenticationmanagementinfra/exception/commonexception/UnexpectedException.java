package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class UnexpectedException extends AuthenticationException {
    public UnexpectedException(ExceptionType exceptionType) {
        super(exceptionType);
    }

    public UnexpectedException(String message) {
        super(ExceptionType.EXCEPTION, message);
    }
}
