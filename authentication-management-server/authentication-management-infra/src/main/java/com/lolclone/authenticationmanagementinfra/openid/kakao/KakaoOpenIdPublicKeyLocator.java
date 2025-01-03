package com.lolclone.authenticationmanagementinfra.openid.kakao;

import java.security.Key;

import org.springframework.stereotype.Component;

import com.lolclone.authenticationmanagementdomain.exception.ExceptionType;
import com.lolclone.authenticationmanagementdomain.exception.OpenIdTokenException;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Locator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoOpenIdPublicKeyLocator implements Locator<Key> {

    private final KakaoOpenIdJwksClient kakaoOpenIdJwksClient;
    private final CachedOpenIdKeyProvider cachedOpenIdKeyProvider;

    @Override
    public Key locate(Header header) {
        String kid = (String) header.get("kid");
        if (kid == null) {
            throw new OpenIdTokenException(ExceptionType.OPEN_ID_INVALID_TOKEN);
        }
        return cachedOpenIdKeyProvider.provide(kid, kakaoOpenIdJwksClient::requestGetJwks);
    }
}
