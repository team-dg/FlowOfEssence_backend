package com.lolclone.authentication_management_server.common.commonexception;

import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;

public class ForbiddenException extends AuthenticationException {
    public ForbiddenException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
