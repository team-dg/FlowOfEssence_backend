package com.lolclone.chatinfra.exception.commonexception;

import org.springframework.core.NestedRuntimeException;

import com.lolclone.chatinfra.exception.domain.ExceptionType;

import lombok.Getter;

@Getter
public abstract class ChatException extends NestedRuntimeException{
    private final ExceptionType exceptionType;

    protected ChatException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    protected ChatException(final ExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        this.exceptionType = exceptionType;
    }

    protected ChatException(final ExceptionType exceptionType, final String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
