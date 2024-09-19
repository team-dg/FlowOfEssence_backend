package com.lolclone.common_module.commonexception;

import com.lolclone.common_module.exception.domain.ExceptionType;

public class ForbiddenException extends FlowOfEssenceException {
    public ForbiddenException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
