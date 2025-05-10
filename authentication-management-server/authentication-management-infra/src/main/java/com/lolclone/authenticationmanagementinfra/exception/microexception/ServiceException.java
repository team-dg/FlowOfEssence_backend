package com.lolclone.authenticationmanagementinfra.exception.microexception;

import org.springframework.core.NestedRuntimeException;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

import lombok.Getter;

/**
 * 외부 서비스와의 통신 중 발생하는 기본 예외 클래스입니다.
 */
@Getter
public abstract class ServiceException extends NestedRuntimeException {
    private final ExceptionType exceptionType;

    protected ServiceException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    protected ServiceException(final ExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        this.exceptionType = exceptionType;
    }

    protected ServiceException(final ExceptionType exceptionType, final String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
} 