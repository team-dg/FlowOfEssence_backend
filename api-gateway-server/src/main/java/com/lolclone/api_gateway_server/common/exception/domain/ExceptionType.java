package com.lolclone.api_gateway_server.common.exception.domain;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionType {
    //401
    NOT_BEARER_TOKEN_TYPE(UNAUTHORIZED, "E001", "Bearer 타입의 토큰이 아닙니다."),
    NEED_AUTH_TOKEN(UNAUTHORIZED, "E002", "로그인이 필요한 서비스입니다."),
    EXPIRED_AUTH_TOKEN(UNAUTHORIZED, "E004", "만료된 로그인 토큰입니다."),
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "E005", "유효하지 않은 로그인 토큰입니다."),

    //403
    NOT_ENOUGH_PERMISSION(UNAUTHORIZED, "E003", "해당 권한이 없습니다."),

    //500
    EXCEPTION(INTERNAL_SERVER_ERROR, "E000", "예상치 못한 오류가 발생했습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
