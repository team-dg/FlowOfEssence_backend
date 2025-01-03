package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class MemberNotFoundException extends AuthenticationException {
    public MemberNotFoundException(final ExceptionType exceptionType, final String message) {
        super(exceptionType, message);
    }
}
