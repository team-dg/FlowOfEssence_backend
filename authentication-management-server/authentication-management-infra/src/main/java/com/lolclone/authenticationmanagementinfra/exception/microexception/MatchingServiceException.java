package com.lolclone.authenticationmanagementinfra.exception.microexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

/**
 * 매칭 서비스와의 통신 중 발생하는 예외를 나타냅니다.
 */
public class MatchingServiceException extends ServiceException {

    public MatchingServiceException(final ExceptionType exceptionType) {
        super(exceptionType);
    }

    public MatchingServiceException(final ExceptionType exceptionType, final String message) {
        super(exceptionType, message);
    }

    public MatchingServiceException(final ExceptionType exceptionType, final Throwable cause) {
        super(exceptionType, cause);
    }
} 