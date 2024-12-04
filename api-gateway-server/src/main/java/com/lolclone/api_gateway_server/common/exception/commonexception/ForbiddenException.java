package com.lolclone.api_gateway_server.common.exception.commonexception;

import com.lolclone.api_gateway_server.common.exception.domain.ExceptionType;

public class ForbiddenException extends ApiGateWayException{
    public ForbiddenException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
