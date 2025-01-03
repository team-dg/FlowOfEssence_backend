package com.lolclone.authenticationmanagementdomain.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionType {
    //Member
    INVALID_NICKNAME_MAX_NUMBER("E001", "닉네임은 최소 한자 이상 작성해야됭습니다."),
    
    //OAuth2
    OAUTH2_PROVIDER_NOT_RESPONSE("E002", "OAuth2 제공자가 응답하지 않습니다."),
    OAUTH2_SERVER_ERROR("E003", "OAuth2 서버에서 예상하지 못한 오류가 발생했습니다."),
    OAUTH2_INVALID_TOKEN("E004", "OAuth2 토큰 요청이 유효하지 않습니다: "),
    OAUTH2_DUPLICATE_SOCIAL_TYPE("E005", "동일한 OAuth2 타입이 이미 사용되고 있습니다."),
    OAUTH2_NOT_SUPPORTED_SOCIAL_TYPE("E006", "지원하지 않는 소셜 로그인 타입입니다."),
    
    //OpenID
    OPEN_ID_PROVIDER_NOT_RESPONSE("E007", "OpenID 제공자가 응답하지 않습니다."),
    OPEN_ID_SERVER_ERROR("E008", "OpenID 서버에서 오류가 발생했습니다."),
    OPEN_ID_INVALID_TOKEN("E009", "OpenID 토큰이 유효하지 않습니다."),
    OPEN_ID_DUPLICATE_SOCIAL_TYPE("E010", "동일한 OpenID 타입이 이미 사용되고 있습니다."),
    OPEN_ID_NOT_SUPPORTED_SOCIAL_TYPE("E011", "지원하지 않는 소셜 로그인 타입입니다."),
    ;

    private final String code;
    private final String message;
}
