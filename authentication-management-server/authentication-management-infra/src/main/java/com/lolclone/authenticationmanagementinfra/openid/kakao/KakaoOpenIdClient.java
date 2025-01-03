package com.lolclone.authenticationmanagementinfra.openid.kakao;

import java.time.Clock;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lolclone.authenticationmanagementdomain.domain.openid.OpenIdClient;
import com.lolclone.authenticationmanagementdomain.domain.openid.OpenIdNonceValidator;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.UserInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KakaoOpenIdClient implements OpenIdClient {

    private static final String SUPPORTER_URL = "https://kauth.kakao.com";
    private final OpenIdNonceValidator openIdNonceValidator;
    private final OpenIdTokenParser idTokenParser;
    private final String clientId;

    public KakaoOpenIdClient(
            @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String kakaoClientId,
            KakaoOpenIdPublicKeyLocator kakaoOpenIdPublicKeyLocator,
            OpenIdNonceValidator openIdNonceValidator,
            Clock clock
    ) {
        this.clientId = kakaoClientId;
        this.openIdNonceValidator = openIdNonceValidator;
        this.idTokenParser = new OpenIdTokenParser(Jwts.parser()
                .keyLocator(kakaoOpenIdPublicKeyLocator)
                .requireAudience(clientId)
                .requireIssuer(SUPPORTER_URL)
                .clock(() -> Date.from(clock.instant()))
                .build());
    }

    @Override
    public UserInfo getUserInfo(String idToken) {
        Claims payload = idTokenParser.parse(idToken);
        openIdNonceValidator.validate(payload.get("nonce", String.class), payload.getExpiration());
        return UserInfo.builder()
                .socialType(SocialType.KAKAO)
                .socialId(payload.getSubject())
                .build();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}
