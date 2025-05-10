package com.lolclone.authenticationmanagementinfra.exception.microexception;

import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

/**
 * 채팅 서비스와의 통신 중 발생하는 예외를 나타냅니다.
 */
public class ChatServiceException extends ServiceException {

    public ChatServiceException(final ExceptionType exceptionType) {
        super(exceptionType);
    }

    public ChatServiceException(final ExceptionType exceptionType, final String message) {
        super(exceptionType, message);
    }

    public ChatServiceException(final ExceptionType exceptionType, final Throwable cause) {
        super(exceptionType, cause);
    }
} 