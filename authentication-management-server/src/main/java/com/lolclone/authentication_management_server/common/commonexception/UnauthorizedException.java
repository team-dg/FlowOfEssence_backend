package com.lolclone.authentication_management_server.common.commonexception;

import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;

public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
