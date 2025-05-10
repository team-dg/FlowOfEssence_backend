package com.lolclone.authenticationmanagementinfra.exception.commonexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

public class OAuth2UnlinkException extends AuthenticationException {
    public OAuth2UnlinkException(ExceptionType exceptionType, String providerName) {
        super(exceptionType, String.format("%s 계정 연결 해제에 실패했습니다.", providerName));
    }
}
