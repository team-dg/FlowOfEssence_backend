package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class InternalServerException extends AuthenticationException {
    public InternalServerException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
