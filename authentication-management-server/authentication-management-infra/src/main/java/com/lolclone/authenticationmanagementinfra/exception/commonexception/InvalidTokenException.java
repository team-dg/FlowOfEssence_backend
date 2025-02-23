package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
