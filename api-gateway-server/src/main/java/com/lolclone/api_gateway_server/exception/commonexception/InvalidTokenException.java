package com.lolclone.api_gateway_server.exception.commonexception;

import com.lolclone.api_gateway_server.exception.domain.ExceptionType;

public class InvalidTokenException extends ApiGatewayException {
    public InvalidTokenException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
