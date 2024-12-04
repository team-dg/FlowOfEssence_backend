package com.lolclone.chat_server.exception.common;

import org.springframework.core.NestedRuntimeException;

import com.lolclone.chat_server.exception.domain.ExceptionType;
import lombok.Getter;

@Getter
public abstract class ChatException extends NestedRuntimeException {
    private final ExceptionType exceptionType;

    protected ChatException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    protected ChatException(ExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        this.exceptionType = exceptionType;
    }

    protected ChatException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
} 