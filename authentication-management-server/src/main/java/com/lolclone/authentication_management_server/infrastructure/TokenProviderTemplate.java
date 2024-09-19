package com.lolclone.authentication_management_server.infrastructure;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.UnaryOperator;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lolclone.authentication_management_server.dto.TokenResponse;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProviderTemplate {
    private final SecretKey secretKey;
    private final Clock clock;

    public TokenProviderTemplate(
        @Value("${secret.key}") String secretKey,
        Clock clock
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.clock = clock;
    }

    public TokenResponse provide(long expirationMinutes, UnaryOperator<JwtBuilder> template) {
        Instant now = clock.instant();
        Instant expiredAt = now.plus(expirationMinutes, ChronoUnit.MINUTES);
        JwtBuilder builder = Jwts.builder()
            .setExpiration(Date.from(expiredAt))
            .setIssuedAt(Date.from(now))
            .signWith(secretKey);
        template.apply(builder);
        String accessToken = builder.compact();
        LocalDateTime expiredDateTime = LocalDateTime.ofInstant(expiredAt, clock.getZone());
        return new TokenResponse(accessToken, expiredDateTime);
    }
}
