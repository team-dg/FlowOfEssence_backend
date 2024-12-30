package com.lolclone.authenticationmanagementinfra.exception.domain;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionType {
    //400
    INVALID_REQUEST_ARGUMENT(BAD_REQUEST, "E001", "잘못된 요청입니다."),
    VALIDATION_FAIL(BAD_REQUEST, "E002", "검증에 실패했습니다."),
    DUPLICATED_USERNAME(BAD_REQUEST, "E003", "이미 사용중인 아이디입니다."),
    DUPLICATED_EMAIL(BAD_REQUEST, "E004", "이미 사용중인 이메일입니다."),
    
    //401
    INVALID_CREDENTIALS(UNAUTHORIZED, "E005", "유효하지 않은 자격 증명입니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "E006", "탈취 가능성이 있는 리프레쉬 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "E007", "만료된 리프레쉬 토큰입니다."),
    
    //403
    USER_NOT_FOUND(NOT_FOUND, "E008", "존재하지 않는 유저입니다."),

    //500
    EXCEPTION(INTERNAL_SERVER_ERROR, "E000", "예상치 못한 오류가 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
