package com.lolclone.api_gateway_server.exception.domain;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
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
    UNHANDLED_EXCEPTION(BAD_REQUEST, "E005", "예상치 못한 예외가 발생했습니다."),

    //401
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "E001", "Refresh Token이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "E002", "Refresh Token이 유효하지 않습니다."),

    //403
    INVALID_REDIS_KEY(FORBIDDEN, "E003", "Redis 키가 유효하지 않습니다."),
    INVALID_CREDENTIALS(FORBIDDEN, "E004", "유효하지 않은 자격 증명입니다."),

    //404
    JWT_CLAIM_NOT_FOUND(NOT_FOUND, "E003", "JWT 클레임이 존재하지 않습니다."),
    AUTHENTICATION_PROVIDER_NOT_FOUND(NOT_FOUND, "E004", "인증 제공자를 찾을 수 없습니다."),

    //500
    SERIALIZATION_EXCEPTION(INTERNAL_SERVER_ERROR, "E005", "직렬화 예외가 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
