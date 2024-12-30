package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class NotFoundException extends AuthenticationException {
    public NotFoundException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
