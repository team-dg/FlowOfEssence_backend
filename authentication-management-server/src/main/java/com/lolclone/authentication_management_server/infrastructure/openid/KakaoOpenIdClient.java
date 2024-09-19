package com.lolclone.authentication_management_server.infrastructure.openid;

import com.lolclone.authentication_management_server.domain.OpenIdClient;
import com.lolclone.authentication_management_server.domain.OpenIdNonceValidator;
import com.lolclone.common_module.commonexception.UnauthorizedException;
import com.lolclone.common_module.exception.domain.ExceptionType;
import com.lolclone.database_server.authenticationserver.domain.SocialType;
import com.lolclone.database_server.authenticationserver.domain.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Date;

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
