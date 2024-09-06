package com.lolclone.logging_monitoring_server.exception.domain;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionType {
    EXCEPTION(INTERNAL_SERVER_ERROR, "E000", "An unexpected error has occurred."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
