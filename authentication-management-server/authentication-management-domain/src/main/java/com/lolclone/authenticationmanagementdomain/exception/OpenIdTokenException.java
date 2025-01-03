package com.lolclone.authenticationmanagementdomain.exception;

public class OpenIdTokenException extends RuntimeException {
    public OpenIdTokenException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }
}
