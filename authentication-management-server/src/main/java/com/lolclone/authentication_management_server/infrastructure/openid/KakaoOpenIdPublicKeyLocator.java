package com.lolclone.authentication_management_server.infrastructure.openid;

import com.lolclone.authentication_management_server.common.commonexception.UnauthorizedException;
import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Locator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
public class KakaoOpenIdPublicKeyLocator implements Locator<Key> {

    private final KakaoOpenIdJwksClient kakaoOpenIdJwksClient;
    private final CachedOpenIdKeyProvider cachedOpenIdKeyProvider;

    @Override
    public Key locate(Header header) {
        String kid = (String) header.get("kid");
        if (kid == null) {
            throw new UnauthorizedException(ExceptionType.OPEN_ID_INVALID_TOKEN);
        }
        return cachedOpenIdKeyProvider.provide(kid, kakaoOpenIdJwksClient::requestGetJwks);
    }
}
