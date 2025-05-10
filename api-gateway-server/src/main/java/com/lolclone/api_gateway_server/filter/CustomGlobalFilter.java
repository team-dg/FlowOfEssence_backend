package com.lolclone.api_gateway_server.filter;

import java.util.Arrays;
import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.lolclone.api_gateway_server.domain.TokenClaims;
import com.lolclone.api_gateway_server.exception.commonexception.InvalidTokenException;
import com.lolclone.api_gateway_server.exception.domain.ExceptionType;
import com.lolclone.api_gateway_server.service.JwtTokenParser;
import com.lolclone.api_gateway_server.utils.ReactiveRedisUtil;
import com.lolclone.commonmodule.domain.SessionStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


// 여기 지금 토큰 검증하고 검증한 데이터 넘겨주기 해야 됨 -> 각 서버에서 인터셉터를 통해서 헤더에서 인증된 사용자 추출하고 각 서버에서 security를 통해서 인증된 사용자를 관리하도록 수정해야 됨 여기서 인증 객체를 저장해봤자 관리가 안됨
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomGlobalFilter implements GlobalFilter, Ordered{
    private final JwtTokenParser jwtTokenParser;
    private final ReactiveRedisUtil redisUtil;

    private final List<String> excludePaths = Arrays.asList(
        "/auth/login",
        "/auth/signup",
        "/auth/refresh"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if(isExcludedPath(path))
            return chain.filter(exchange);

        return getToken(exchange)
            .flatMap(token -> validateToken(token)
                .flatMap(claims -> {
                    String memberId = claims.getId().toString();

                    // 세션 상태 확인
                    return validateSession(memberId)
                        .filter(validSession -> {
                            if (!validSession) {
                                throw new InvalidTokenException(ExceptionType.INVALID_CREDENTIALS);
                            }
                            return true;
                        })
                        .doOnNext(validSession -> redisUtil.updateSessionLastAccessTime(memberId))
                        .then(chain.filter(exchange));
                })
            )
            .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }

    private boolean isExcludedPath(String path) {
        return excludePaths.stream()
            .anyMatch(path::startsWith);
    }

    private Mono<String> getToken(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return token != null ? Mono.just(token) : Mono.empty();
    }

    private Mono<TokenClaims> validateToken(String token) {
        // 1. 블랙리스트 확인
        return redisUtil.isBlacklistToken(token)
            .flatMap(isBlacklisted -> {
              if (isBlacklisted) {
                return Mono.error(new InvalidTokenException(ExceptionType.INVALID_CREDENTIALS));
              }
              // 2. 토큰 검증 및 클레임 추출
              return jwtTokenParser.verifyAndGetClaims(token);
            });
    }

    private Mono<Boolean> validateSession(String memberId) {
        return redisUtil.getUserSession(memberId)
            .flatMap(session -> {
                // 1. 세션 상태 확인
                if (session.getStatus() != SessionStatus.ACTIVE) {
                    log.warn("비활성 세션: memberId={}, status={}", memberId, session.getStatus());
                    return Mono.just(false);
                }
              
                // 2. 세션 타임아웃 확인
                //   Instant lastAccessTime = session.getLastAccessTime();
                //   Instant currentTime = Instant.now();
                //   Duration inactivityDuration = Duration.between(lastAccessTime, currentTime);
                
                //   if (inactivityDuration.toMinutes() > sessionInactivityTimeoutMinutes) {
                //     log.warn("세션 타임아웃: memberId={}, lastAccessTime={}", memberId, lastAccessTime);
                //     return Mono.just(false);
                //   }
              
                return Mono.just(true);
            })
            .defaultIfEmpty(false);
      }
}
