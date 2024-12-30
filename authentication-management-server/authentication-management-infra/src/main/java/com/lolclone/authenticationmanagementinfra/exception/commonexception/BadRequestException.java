package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class BadRequestException extends AuthenticationException {
    public BadRequestException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
