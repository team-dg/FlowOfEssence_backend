package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class TokenExpiredException extends org.springframework.security.core.AuthenticationException {
    public TokenExpiredException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }
}
