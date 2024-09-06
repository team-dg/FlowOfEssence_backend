package com.lolclone.logging_monitoring_server.exception.domain;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String code;
    private final String message;

    public BusinessException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.status = exceptionType.getStatus();
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
    }
    
    public BusinessException(final ExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        this.status = exceptionType.getStatus();
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
    }
}
