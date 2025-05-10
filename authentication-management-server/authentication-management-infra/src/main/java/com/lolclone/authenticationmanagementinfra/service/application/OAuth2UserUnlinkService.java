package com.lolclone.authenticationmanagementinfra.service.application;

import java.util.UUID;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Properties;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;
import com.lolclone.authenticationmanagementinfra.infrastructure.GoogleOAuth2UserUnlink;
import com.lolclone.authenticationmanagementinfra.infrastructure.KakaoOAuth2UserUnlink;
import com.lolclone.authenticationmanagementinfra.infrastructure.NaverOAuth2UserUnlink;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserUnlinkService {
    private final OAuth2Properties oauth2Properties;
    private final RestTemplate restTemplate;

    public void unlink(final OAuth2Provider provider, final UUID memberId, final String accessToken) {
        switch (provider) {
            case GOOGLE -> new GoogleOAuth2UserUnlink(oauth2Properties, restTemplate).unlink(memberId, accessToken);
            case KAKAO -> new KakaoOAuth2UserUnlink(oauth2Properties, restTemplate).unlink(memberId, accessToken);
            case NAVER -> new NaverOAuth2UserUnlink(oauth2Properties, restTemplate).unlink(memberId, accessToken);
            default -> throw new OAuth2AuthenticationException(new OAuth2Error("unlink_error 지원하지 않는 소셜 프로바이더: " + provider));
        };
    }
}
