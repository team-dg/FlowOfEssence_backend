package com.lolclone.api_gateway_server.common.exception.commonexception;

import com.lolclone.api_gateway_server.common.exception.domain.ExceptionType;

public class BadRequestException extends ApiGateWayException {
    public BadRequestException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
