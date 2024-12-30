package com.lolclone.authenticationmanagementdomain.exception;

public class OpenIdClientException extends RuntimeException {
    public OpenIdClientException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }
}
