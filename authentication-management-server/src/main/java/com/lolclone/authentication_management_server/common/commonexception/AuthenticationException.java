package com.lolclone.authentication_management_server.common.commonexception;

import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;
import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public abstract class AuthenticationException extends NestedRuntimeException {
    private final ExceptionType exceptionType;

    protected AuthenticationException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    protected AuthenticationException(final ExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        this.exceptionType = exceptionType;
    }

    protected AuthenticationException(final ExceptionType exceptionType, final String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
