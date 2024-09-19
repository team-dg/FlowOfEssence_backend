package com.lolclone.common_module.exception.domain;

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
    INVALID_REQUEST_ARGUMENT(BAD_REQUEST, "E022", "잘못된 요청입니다."),
    OAUTH2_NOT_SUPPORTED_SOCIAL_TYPE(BAD_REQUEST, "E001", "지원하지 않는 소셜 로그인 타입입니다."),
    OAUTH2_DUPLICATE_SOCIAL_TYPE(BAD_REQUEST, "E002", "이미 등록된 소셜 로그인 타입입니다."),
    VALIDATION_FAIL(BAD_REQUEST, "E003", "검증에 실패했습니다."),
    OPEN_ID_NOT_SUPPORTED_SOCIAL_TYPE(BAD_REQUEST, "E004", "지원하지 않는 소셜 로그인 타입입니다."),
    OAUTH2_INVALID_CODE(BAD_REQUEST, "E011", "유효하지 않은 OAuth2 코드입니다."),

    //401
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "E005", "유효하지 않은 리프레쉬 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "E006", "만료된 리프레쉬 토큰입니다."),
    OPEN_ID_INVALID_TOKEN(UNAUTHORIZED, "E007", "유효하지 않은 OpenID 토큰입니다."),
    OAUTH2_TOKEN_ERROR(UNAUTHORIZED, "E014", "OAuth2 토큰 에러입니다."),
    NEED_AUTH_TOKEN(UNAUTHORIZED, "E015", "로그인이 필요한 서비스입니다."),
    EXPIRED_AUTH_TOKEN(UNAUTHORIZED, "E017", "만료된 로그인 토큰입니다."),
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "E018", "유효하지 않은 로그인 토큰입니다."),
    NOT_BEARER_TOKEN_TYPE(UNAUTHORIZED, "E019", "Bearer 타입의 토큰이 아닙니다."),
    INVALID_NICKNAME_MAX_NUMBER(UNAUTHORIZED, "E020", "닉네임 최대 길이는 10000자 이하여야 합니다."),
    INVALID_CREDENTIALS(UNAUTHORIZED, "E021", "유효하지 않은 자격 증명입니다."),
    DUPLICATED_USERNAME(UNAUTHORIZED, "E023", "이미 사용중인 아이디입니다."),
    DUPLICATED_EMAIL(UNAUTHORIZED, "E024", "이미 사용중인 이메일입니다."),
    INVALID_PASSWORD(UNAUTHORIZED, "E025", "비밀번호가 유효하지 않습니다."),

    //403
    NOT_ENOUGH_PERMISSION(UNAUTHORIZED, "E016", "해당 권한이 없습니다."),

    //404
    USER_NOT_FOUND(NOT_FOUND, "E008", "존재하지 않는 유저입니다."),

    //500
    EXCEPTION(INTERNAL_SERVER_ERROR, "E000", "예상치 못한 오류가 발생했습니다."),
    OPEN_ID_PROVIDER_NOT_RESPONSE(INTERNAL_SERVER_ERROR, "E009", "OpenID 제공자가 응답하지 않습니다."),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "E010", "내부 서버 오류가 발생했습니다."),
    OAUTH2_INVALID_REQUEST(INTERNAL_SERVER_ERROR, "E012", "OAuth2 요청에 에러가 발생했습니다"),
    OAUTH2_PROVIDER_NOT_RESPONSE(INTERNAL_SERVER_ERROR, "E013", "OAuth2 제공자가 응답하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
