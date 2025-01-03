package com.lolclone.userinfra.exception.commonexception;

import org.springframework.core.NestedRuntimeException;

import com.lolclone.userinfra.exception.domain.ExceptionType;

import lombok.Getter;

@Getter
public abstract class UserException extends NestedRuntimeException {
    private final ExceptionType exceptionType;

    protected UserException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    protected UserException(final ExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        this.exceptionType = exceptionType;
    }

    protected UserException(final ExceptionType exceptionType, final String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
