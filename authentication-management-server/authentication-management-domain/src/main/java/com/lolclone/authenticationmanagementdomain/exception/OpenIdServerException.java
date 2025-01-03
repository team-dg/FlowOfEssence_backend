package com.lolclone.authenticationmanagementdomain.exception;

public class OpenIdServerException extends RuntimeException {
    public OpenIdServerException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }
}
