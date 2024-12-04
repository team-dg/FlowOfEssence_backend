package com.lolclone.authentication_management_server.common.commonexception;

import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;

public class BadRequestException extends AuthenticationException {
    public BadRequestException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
