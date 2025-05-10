package com.lolclone.api_gateway_server.exception.commonexception;

import com.lolclone.api_gateway_server.exception.domain.ExceptionType;

public class TokenExpiredException extends ApiGatewayException {
    public TokenExpiredException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
