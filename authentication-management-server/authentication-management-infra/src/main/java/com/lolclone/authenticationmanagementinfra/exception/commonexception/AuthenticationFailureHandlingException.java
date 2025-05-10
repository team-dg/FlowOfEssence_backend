package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class AuthenticationFailureHandlingException extends AuthenticationException {
    public AuthenticationFailureHandlingException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
