package com.lolclone.chatinfra.exception.domain;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionType {
    //400
    INVALID_REQUEST_ARGUMENT(HttpStatus.BAD_REQUEST, "E001", "잘못된 요청입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "E002", "존재하지 않는 사용자입니다."),

    //500
    EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "E003", "예상치 못한 오류가 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
