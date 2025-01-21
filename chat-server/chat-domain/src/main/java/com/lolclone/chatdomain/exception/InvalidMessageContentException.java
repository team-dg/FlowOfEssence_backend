package com.lolclone.chatdomain.exception;

public class InvalidMessageContentException extends RuntimeException{
    public InvalidMessageContentException(String message) {
        super(message);
    }
}
