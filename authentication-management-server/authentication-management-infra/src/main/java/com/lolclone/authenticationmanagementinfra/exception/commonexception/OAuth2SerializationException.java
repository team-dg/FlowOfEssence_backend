package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class OAuth2SerializationException extends AuthenticationException {
    public OAuth2SerializationException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
