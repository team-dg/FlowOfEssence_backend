package com.lolclone.commonmodule.exception;

public class UnexpectedException extends RuntimeException{
    private final String code;

    public UnexpectedException(String message, String code) {
        super(message);
        this.code = code;
    }
}
