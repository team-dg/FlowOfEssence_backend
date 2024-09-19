package com.lolclone.common_module.commonexception;

import com.lolclone.common_module.exception.domain.ExceptionType;

public class UnexpectedException extends FlowOfEssenceException {
    public UnexpectedException(ExceptionType exceptionType) {
        super(exceptionType);
    }

    public UnexpectedException(String message) {
        super(ExceptionType.EXCEPTION, message);
    }
}
