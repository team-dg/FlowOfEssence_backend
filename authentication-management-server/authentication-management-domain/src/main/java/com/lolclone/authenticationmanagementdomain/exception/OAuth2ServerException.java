package com.lolclone.authenticationmanagementdomain.exception;

public class OAuth2ServerException extends RuntimeException {
    public OAuth2ServerException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }
}
