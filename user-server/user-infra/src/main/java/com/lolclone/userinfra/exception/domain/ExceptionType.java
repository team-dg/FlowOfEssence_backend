package com.lolclone.userinfra.exception.domain;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionType {
    //400
    INVALID_REQUEST_ARGUMENT(BAD_REQUEST, "E001", "잘못된 요청입니다."),
    //401

    //403

    //404

    //500

    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}
