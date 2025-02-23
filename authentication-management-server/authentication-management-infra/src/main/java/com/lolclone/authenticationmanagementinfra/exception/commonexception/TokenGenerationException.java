package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class TokenGenerationException extends AuthenticationException {
    public TokenGenerationException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
