package com.lolclone.api_gateway_server.common.exception.commonexception;

import com.lolclone.api_gateway_server.common.exception.domain.ExceptionType;

public class UnexpectedException extends ApiGateWayException{
    public UnexpectedException(ExceptionType exceptionType) {
        super(exceptionType);
    }

    public UnexpectedException(String message) {
        super(ExceptionType.EXCEPTION, message);
    }
}
