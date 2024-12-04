package com.lolclone.authentication_management_server.common.commonexception;

import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;

public class ValidException extends AuthenticationException {
    public ValidException(final String message) {
        super(ExceptionType.VALIDATION_FAIL, message);
    }
}
