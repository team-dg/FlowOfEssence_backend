package com.lolclone.authenticationmanagementdomain.exception;

public class NicknameGenerationException extends RuntimeException {
    public NicknameGenerationException() {
        super(ExceptionType.INVALID_NICKNAME_MAX_NUMBER.getMessage());
    }
}
