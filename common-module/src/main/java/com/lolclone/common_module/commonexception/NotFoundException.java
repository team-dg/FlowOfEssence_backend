package com.lolclone.common_module.commonexception;

import com.lolclone.common_module.exception.domain.ExceptionType;

public class NotFoundException extends FlowOfEssenceException {
    public NotFoundException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
