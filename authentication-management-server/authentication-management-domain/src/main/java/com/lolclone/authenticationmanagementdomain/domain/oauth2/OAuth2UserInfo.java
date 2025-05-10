package com.lolclone.authenticationmanagementdomain.domain.oauth2;

import java.util.Collections;
import java.util.Map;

/**
 * 소셜 사용자 정보의 공통 인터페이스
 * OAuth2, OIDC 구분 없이 "우리가 필요한" 기본 정보만 선언
 */
public interface OAuth2UserInfo {
    // Provider 별 고유 속성을 위한 메서드 추가
    default Map<String, Object> getProviderSpecificAttributes() {
        return Collections.emptyMap();
    }

    // 추가 정보 존재 여부 확인
    default boolean hasAdditionalAttributes() {
        return !getProviderSpecificAttributes().isEmpty();
    }

    /**
     * 어떤 소셜/IDP에서 왔는지 식별 (ex: "Google", "Naver", "kakao")
     */
    OAuth2Provider getProvider();

    /**
     * 사용자 접근 토큰
    */
    String getAccessToken();

    /**
     * 사용자 정보 추가 속성
     */
    Map<String, Object> getAttributes();

    /**
     * 소셜/IDP 고유 사용자 식별자
     * - OAuth2: Naver -> "id", Kakao -> "id"
     * - OIDC: Google -> "sub"
     */
    String getSocialId();

    /**
     * 사용자 이메일
     */
    String getEmail();

    /**
     * 사용자 이름
     */
    String getName();

    /**
     * 사용자 이름 중 첫 글자
     */
    String getFirstName();

    /**
     * 사용자 이름 중 마지막 글자
     */
    String getLastName();

    /**
     * 사용자 닉네임
     */
    String getNickname();
    
    /**
     * 사용자 프로필 이미지 URL
     */
    String getProfileImageUrl();
}
