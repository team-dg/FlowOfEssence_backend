package com.lolclone.api_gateway_server.exception.commonexception;

import com.lolclone.api_gateway_server.exception.domain.ExceptionType;

public class SerializationException extends ApiGatewayException {
    public SerializationException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
