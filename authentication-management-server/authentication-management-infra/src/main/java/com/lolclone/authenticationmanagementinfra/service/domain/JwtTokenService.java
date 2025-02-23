package com.lolclone.authenticationmanagementinfra.service.domain;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementdomain.domain.TokenClaims;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.JwtClaimNotFoundException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.service.TokenProviderTemplate;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenRefreshResponse;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenResponse;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenService {
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PROVIDER = "provider";
    private static final String AUTHORITIES = "authorities";
    private final TokenProviderTemplate tokenProviderTemplate;

    public TokenRefreshResponse createTokenResponse(Member member, OAuth2Provider provider) {
        String accessToken = tokenProviderTemplate.createAccessToken(
            jwtBuilder -> jwtBuilder.claims()
                .add(ID, member.getId())
                .add(EMAIL, member.getEmail())
                .add(PROVIDER, provider.getRegistrationId())
                .add(AUTHORITIES, member.getAuthorityStrings())
                .and()
        );

        String refreshToken = tokenProviderTemplate.createRefreshToken(
            jwtBuilder -> jwtBuilder.claims()
                .add(ID, member.getId())
                .add(EMAIL, member.getEmail())
                .add(PROVIDER, provider.getRegistrationId())
                .add(AUTHORITIES, member.getAuthorityStrings())
                .and()
        );
        
        TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse(
            new TokenResponse(accessToken),
            new TokenResponse(refreshToken)
        );

        return tokenRefreshResponse;
    }

    // accessToken만 발급
    public TokenRefreshResponse createAccessTokenResponse(Member member, OAuth2Provider provider, String refreshToken) {
        String newAccessToken = tokenProviderTemplate.createAccessToken(
            jwtBuilder -> jwtBuilder.claims()
                .add(ID, member.getId())
                .add(EMAIL, member.getEmail())
                .add(PROVIDER, provider.getRegistrationId())
                .add(AUTHORITIES, member.getAuthorityStrings())
                .and()
        );

        TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse(
            new TokenResponse(newAccessToken),
            new TokenResponse(refreshToken)
        );

        return tokenRefreshResponse;
    }

    public TokenClaims verifyAndGetClaims(String token) {
        Claims claims = tokenProviderTemplate.verifyAndGetClaims(token);
        return convertToTokenClaims(claims);
    }

    private TokenClaims convertToTokenClaims(final Claims claims) {
        return new TokenClaims(
            Optional.ofNullable(claims.get(ID, UUID.class))
                .orElseThrow(() -> new JwtClaimNotFoundException(ExceptionType.JWT_CLAIM_NOT_FOUND)),
            Optional.ofNullable(claims.get(EMAIL, String.class))
                .orElseThrow(() -> new JwtClaimNotFoundException(ExceptionType.JWT_CLAIM_NOT_FOUND)),
            Optional.ofNullable(claims.get(PROVIDER, String.class))
                    .flatMap(OAuth2Provider::fromRegistrationId)
                    .orElse(OAuth2Provider.DEFAULT),
            getAuthorityStrings(claims)
        );
    }

    @SuppressWarnings("unchecked")
    private List<String> getAuthorityStrings(Claims claims) {
        Object authorities = claims.get(AUTHORITIES);
        if (authorities instanceof List<?>) {
            if (((List<?>) authorities).stream().allMatch(String.class::isInstance)) {
                return (List<String>) authorities;
            }
        }
        return Collections.emptyList(); // 안전하게 빈 리스트 반환
    }
}
