package com.lolclone.api_gateway_server.exception.commonexception;

import org.springframework.core.NestedRuntimeException;

import com.lolclone.api_gateway_server.exception.domain.ExceptionType;

import lombok.Getter;

@Getter
public abstract class ApiGatewayException extends NestedRuntimeException {
    private final ExceptionType exceptionType;

    protected ApiGatewayException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    protected ApiGatewayException(final ExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        this.exceptionType = exceptionType;
    }

    protected ApiGatewayException(final ExceptionType exceptionType, final String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
