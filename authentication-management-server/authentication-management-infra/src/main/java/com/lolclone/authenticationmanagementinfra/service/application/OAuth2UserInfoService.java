package com.lolclone.authenticationmanagementinfra.service.application;

import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;

import com.lolclone.authenticationmanagementdomain.domain.oauth2.GoogleOAuth2UserInfo;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.KakaoOAuth2UserInfo;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.NaverOAuth2UserInfo;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Properties;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2UserInfo;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Properties.ProviderProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserInfoService {
    private final OAuth2Properties oauth2Properties;

    public OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes, String accessToken) {
        OAuth2Provider oauth2Provider = OAuth2Provider.valueOf(provider.toUpperCase());
        ProviderProperties providerProps = oauth2Properties.getProviderProperties(provider);

        return switch (oauth2Provider) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes, accessToken, providerProps);
            case KAKAO -> new KakaoOAuth2UserInfo(attributes, accessToken, providerProps);
            case NAVER -> new NaverOAuth2UserInfo(attributes, accessToken, providerProps);
            default -> throw new OAuth2AuthenticationException(new OAuth2Error("해당 소셜 로그인은 지원하지 않습니다 : " + oauth2Provider));
        };
    }
}
