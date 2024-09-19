package com.lolclone.common_module.commonexception;

import com.lolclone.common_module.exception.domain.ExceptionType;

public class InternalServerException extends FlowOfEssenceException {
    public InternalServerException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
