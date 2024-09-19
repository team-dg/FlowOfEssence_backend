package com.lolclone.common_module.commonexception;

import com.lolclone.common_module.exception.domain.ExceptionType;

public class UnauthorizedException extends FlowOfEssenceException {
    public UnauthorizedException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
