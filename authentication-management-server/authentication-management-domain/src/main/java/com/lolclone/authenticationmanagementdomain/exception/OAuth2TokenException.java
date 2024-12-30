package com.lolclone.authenticationmanagementdomain.exception;

public class OAuth2TokenException extends RuntimeException {
    public OAuth2TokenException() {
        super(ExceptionType.OAUTH2_INVALID_TOKEN.getMessage());
    }
}
