package com.lolclone.api_gateway_server.common.exception.commonexception;

import com.lolclone.api_gateway_server.common.exception.domain.ExceptionType;

public class UnauthorizedException extends ApiGateWayException {
    public UnauthorizedException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
