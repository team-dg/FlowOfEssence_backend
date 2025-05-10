package com.lolclone.api_gateway_server.service;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lolclone.api_gateway_server.domain.TokenClaims;
import com.lolclone.api_gateway_server.exception.commonexception.InvalidTokenException;
import com.lolclone.api_gateway_server.exception.commonexception.JwtClaimNotFoundException;
import com.lolclone.api_gateway_server.exception.commonexception.TokenExpiredException;
import com.lolclone.api_gateway_server.exception.domain.ExceptionType;
import com.lolclone.commonmodule.domain.OAuth2Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class JwtTokenParser {
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PROVIDER = "provider";
    private static final String AUTHORITIES = "authorities";
    private static final String BEARER_PREFIX = "Bearer ";
    
    private final SecretKey secretKey;
    private final Clock clock;

    public JwtTokenParser(
        @Value("${secret.key}")String secretKey, 
        Clock clock
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.clock = clock;
    }

    public Mono<TokenClaims> verifyAndGetClaims(String token) throws JwtException {
        return Mono.defer(() -> 
            Mono.just(token)
                .map(this::extractToken)
                .map(extractToken -> Jwts.parser()
                    .verifyWith(secretKey)
                    .clockSkewSeconds(60)
                    .clock(() -> Date.from(clock.instant()))
                    .build()
                    .parseSignedClaims(extractToken)
                    .getPayload())
                .map(this::convertToTokenClaims)
                .onErrorMap(ExpiredJwtException.class,
                    e -> new TokenExpiredException(ExceptionType.EXPIRED_REFRESH_TOKEN))
                .onErrorMap(JwtException.class,
                    e -> new InvalidTokenException(ExceptionType.INVALID_REFRESH_TOKEN))
        );
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

    private String extractToken(final String token) {
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return token;
    }
}
