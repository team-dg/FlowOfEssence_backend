package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class ValidException extends AuthenticationException {
    public ValidException(final String message) {
        super(ExceptionType.VALIDATION_FAIL, message);
    }
}
