package com.lolclone.authentication_management_server.common.commonexception;

import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;

public class InternalServerException extends AuthenticationException {
    public InternalServerException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
