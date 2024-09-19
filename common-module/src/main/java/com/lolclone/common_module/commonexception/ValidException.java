package com.lolclone.common_module.commonexception;

import com.lolclone.common_module.exception.domain.ExceptionType;

public class ValidException extends FlowOfEssenceException {
    public ValidException(final String message) {
        super(ExceptionType.VALIDATION_FAIL, message);
    }
}
