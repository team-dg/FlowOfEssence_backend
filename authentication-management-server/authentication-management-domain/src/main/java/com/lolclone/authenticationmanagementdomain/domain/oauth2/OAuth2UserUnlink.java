package com.lolclone.authenticationmanagementdomain.domain.oauth2;

import java.util.UUID;

/**
 * OAuth2 서비스 제공자의 연결 해제(회원 탈퇴)를 위한 포트
 */
public interface OAuth2UserUnlink {
    /**
     * OAuth2 서비스와의 연결을 해제합니다.
     *
     * @param accessToken 사용자의 액세스 토큰
     * @throws OAuth2UnlinkException 연결 해제 과정에서 오류가 발생한 경우
     */
    void unlink(final UUID memberId, final String accessToken);

    /**
     * 해당 OAuth2UserUnlinkPort 구현체가 처리할 수 있는 
     * OAuth2 서비스 제공자 타입을 반환합니다.
     *
     * @return OAuth2 서비스 제공자 타입
     */
    OAuth2Provider getProvider();
}
