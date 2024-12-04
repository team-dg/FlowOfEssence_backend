package com.lolclone.api_gateway_server.common.exception.commonexception;

import com.lolclone.api_gateway_server.common.exception.domain.ExceptionType;
import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public abstract class ApiGateWayException extends NestedRuntimeException {
    private final ExceptionType exceptionType;

    protected ApiGateWayException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    protected ApiGateWayException(final ExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        this.exceptionType = exceptionType;
    }

    protected ApiGateWayException(final ExceptionType exceptionType, final String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
