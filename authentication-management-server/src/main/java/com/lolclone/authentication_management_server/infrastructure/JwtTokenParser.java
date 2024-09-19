package com.lolclone.authentication_management_server.infrastructure;

import com.lolclone.common_module.commonexception.UnauthorizedException;
import com.lolclone.common_module.exception.domain.ExceptionType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Date;

@Component
@Slf4j
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

    public Claims getClaims(String token) {
        try {
            return jwtParser.parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ExceptionType.EXPIRED_AUTH_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException(ExceptionType.INVALID_AUTH_TOKEN);
        } catch (Exception e) {
            log.error("JWT 토큰 파싱 중에 문제가 발생했습니다.");
            throw e;
        }
    }
}
