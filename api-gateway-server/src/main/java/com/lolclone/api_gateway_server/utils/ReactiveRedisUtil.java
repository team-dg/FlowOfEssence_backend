package com.lolclone.api_gateway_server.utils;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import com.lolclone.commonmodule.domain.UserSession;
import com.lolclone.commonmodule.utils.RedisKeyGenerator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReactiveRedisUtil {
    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private final RedisKeyGenerator keyGenerator;

    public Mono<UserSession> getUserSession(String memberId) {
        String sessionKey = keyGenerator.userSession(memberId);
        return redisTemplate.opsForValue()
            .get(sessionKey)
            .cast(UserSession.class);
    }

    public Mono<Boolean> updateSessionLastAccessTime(String memberId) {
        return getUserSession(memberId)
            .flatMap(session -> {
              session.updateLastAccessTime();
              String sessionKey = keyGenerator.userSession(memberId);
              return redisTemplate.opsForValue().set(sessionKey, session);
            })
            .defaultIfEmpty(false);
    }

    public Mono<String> getBlacklistedTokenOwner(String token) {
        String key = keyGenerator.blacklist(token);
        return redisTemplate.opsForValue()
            .get(key)
            .cast(String.class);
    }

    public Mono<Boolean> isBlacklistToken(String token) {
        String key = keyGenerator.blacklist(token);
        return redisTemplate.hasKey(key);
    }
}
