package com.lolclone.authentication_management_server.common.commonexception;

import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;

public class UnexpectedException extends AuthenticationException {
    public UnexpectedException(ExceptionType exceptionType) {
        super(exceptionType);
    }

    public UnexpectedException(String message) {
        super(ExceptionType.EXCEPTION, message);
    }
}
