package com.lolclone.api_gateway_server.exception.commonexception;

import com.lolclone.api_gateway_server.exception.domain.ExceptionType;

public class AuthenticationException extends ApiGatewayException {
    public AuthenticationException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
