package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class ForbiddenException extends AuthenticationException {
    public ForbiddenException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
