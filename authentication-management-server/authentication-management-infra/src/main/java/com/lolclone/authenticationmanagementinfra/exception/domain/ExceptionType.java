package com.lolclone.authenticationmanagementinfra.exception.domain;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
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
    TOKEN_EXPIRED_EXCEPTION(UNAUTHORIZED, "E008", "토큰이 만료되었습니다."),
    TOKEN_GENERATION_FAILED(UNAUTHORIZED, "E011", "토큰 생성에 실패했습니다."),
    FAIL_LOGIN(UNAUTHORIZED, "E012", "로그인에 실패했습니다."),
    USER_SESSION_EXPIRED(UNAUTHORIZED, "E013", "사용자 세션이 만료되었습니다."),

    //404
    USER_NOT_FOUND(NOT_FOUND, "E008", "존재하지 않는 유저입니다."),
    JWT_CLAIM_NOT_FOUND(NOT_FOUND, "E009", "JWT 클레임이 존재하지 않습니다."),
    JWT_REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "E010", "리프레쉬 토큰이 존재하지 않습니다."),
    USER_SESSION_NOT_FOUND(NOT_FOUND, "E011", "사용자 세션이 존재하지 않습니다."),
    
    //403
    OAUTH2_UNLINK_FAILED(FORBIDDEN, "E009", "OAuth2 계정 연결 해제에 실패했습니다."),
    OAUTH2_SERIALIZATION_EXCEPTION(FORBIDDEN, "E011", "OAuth2 직렬화에 실패했습니다."),
    OAUTH2_DESERIALIZATION_EXCEPTION(FORBIDDEN, "E012", "OAuth2 역직렬화에 실패했습니다."),
    ACCESS_DENIED_EXCEPTION(FORBIDDEN, "E014", "접근이 거부되었습니다."),

    //500
    EXCEPTION(INTERNAL_SERVER_ERROR, "E000", "예상치 못한 오류가 발생했습니다."),
    AUTHENTICATION_ENTRY_POINT_EXCEPTION(INTERNAL_SERVER_ERROR, "E013", "인증 진입점 예외가 발생했습니다."),

    //503
    CHAT_SERVICE_EXCEPTION(SERVICE_UNAVAILABLE, "E014", "채팅 서버 예외가 발생했습니다."),
    MATCHING_SERVICE_EXCEPTION(SERVICE_UNAVAILABLE, "E015", "매칭 서버 예외가 발생했습니다."),
    MICROSERVICE_SERVICE_EXCEPTION(SERVICE_UNAVAILABLE, "E016", "마이크로 서비스 예외가 발생했습니다."),
    SERVICE_COMMUNICATION_ERROR(SERVICE_UNAVAILABLE, "E017", "서비스 통신 오류가 발생했습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
