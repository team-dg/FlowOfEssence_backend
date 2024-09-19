package com.lolclone.common_module.commonexception;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

import com.lolclone.common_module.exception.domain.ExceptionType;

@Getter
public abstract class FlowOfEssenceException extends NestedRuntimeException {
    private final ExceptionType exceptionType;

    protected FlowOfEssenceException(final ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    protected FlowOfEssenceException(final ExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        this.exceptionType = exceptionType;
    }

    protected FlowOfEssenceException(final ExceptionType exceptionType, final String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
