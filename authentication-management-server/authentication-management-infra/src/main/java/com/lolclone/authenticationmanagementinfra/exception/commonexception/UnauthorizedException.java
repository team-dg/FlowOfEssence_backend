package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
