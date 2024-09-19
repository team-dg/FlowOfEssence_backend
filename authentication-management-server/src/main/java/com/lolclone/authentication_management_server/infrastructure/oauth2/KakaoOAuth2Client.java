package com.lolclone.authentication_management_server.infrastructure.oauth2;

import com.lolclone.authentication_management_server.application.OAuth2Client;
import com.lolclone.authentication_management_server.infrastructure.openid.KakaoOpenIdClient;
import com.lolclone.database_server.authenticationserver.domain.SocialType;
import com.lolclone.database_server.authenticationserver.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2Client implements OAuth2Client {

    private final KakaoOpenIdClient kakaoOpenIdClient;
    private final KakaoOAuth2TokenClient kakaoOAuth2TokenClient;

    @Override
    public UserInfo getUserInfo(String code) {
        String idToken = kakaoOAuth2TokenClient.getIdToken(code);
        return kakaoOpenIdClient.getUserInfo(idToken);
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}
