package com.lolclone.api_gateway_server.exception.commonexception;

import com.lolclone.api_gateway_server.exception.domain.ExceptionType;

public class InvalidRedisKeyException extends ApiGatewayException {
    public InvalidRedisKeyException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
