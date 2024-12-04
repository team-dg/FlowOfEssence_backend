package com.lolclone.api_gateway_server.securitys.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Date;

import com.lolclone.api_gateway_server.common.exception.commonexception.UnauthorizedException;
import com.lolclone.api_gateway_server.common.exception.domain.ExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class JwtTokenParser {
    private final JwtParser jwtParser;

    public JwtTokenParser(
        @Value("${secret.key}") String secretKey,
        Clock clock
    ) {
        this.jwtParser = Jwts.parser()
            .clock(() -> Date.from(clock.instant()))
            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build();
    }

    public Mono<Claims> getClaims(String token) {
        return Mono.fromCallable(() -> {
            try {
                return jwtParser.parseSignedClaims(token).getPayload();
            } catch (ExpiredJwtException e) {
                throw new UnauthorizedException(ExceptionType.EXPIRED_AUTH_TOKEN);
            } catch (JwtException | IllegalArgumentException e) {
                throw new UnauthorizedException(ExceptionType.INVALID_AUTH_TOKEN);
            } catch (Exception e) {
                log.error("JWT 토큰 파싱 중에 문제가 발생했습니다.");
                throw e;
            }
        });
    }
}
