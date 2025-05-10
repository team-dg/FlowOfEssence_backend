package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class JwtClaimNotFoundException extends AuthenticationException {
    public JwtClaimNotFoundException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
