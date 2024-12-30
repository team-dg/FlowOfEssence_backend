package com.lolclone.authenticationmanagementinfra.oauth2.kakao;

import org.springframework.stereotype.Component;

import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Client;
import com.lolclone.authenticationmanagementinfra.openid.kakao.KakaoOpenIdClient;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.UserInfo;

import lombok.RequiredArgsConstructor;

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
