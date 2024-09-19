package com.lolclone.common_module.commonexception;

import com.lolclone.common_module.exception.domain.ExceptionType;

public class BadRequestException extends FlowOfEssenceException {
    public BadRequestException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
