package com.lolclone.authenticationmanagementdomain.exception;

public class OAuth2ClientException extends RuntimeException {
    public OAuth2ClientException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }
}
