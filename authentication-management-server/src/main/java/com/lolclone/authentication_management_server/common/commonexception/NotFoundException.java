package com.lolclone.authentication_management_server.common.commonexception;

import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;

public class NotFoundException extends AuthenticationException {
    public NotFoundException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
