package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import org.springframework.core.NestedRuntimeException;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

import lombok.Getter;

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

