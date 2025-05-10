package com.lolclone.commonmodule.utils;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * Redis 키 생성 유틸리티 클래스
 * 
 * 이 클래스는 Redis 키 생성 및 관리에 필요한 유틸리티 메서드를 제공합니다.
 */
@Component
public class RedisKeyGenerator {

    // 1. 상수를 활용한 TTL 매핑 추가
    private final Map<String, Duration> ttlMap = Map.of(
        RedisConstants.Prefix.ACCESS_TOKEN, Duration.ofSeconds(RedisConstants.TTL.ACCESS_TOKEN),
        RedisConstants.Prefix.REFRESH_TOKEN, Duration.ofSeconds(RedisConstants.TTL.REFRESH_TOKEN),
        RedisConstants.Prefix.USER_SESSION, Duration.ofSeconds(RedisConstants.TTL.USER_SESSION),
        RedisConstants.Prefix.BLACKLIST, Duration.ofSeconds(RedisConstants.TTL.BLACKLIST)
    );

    // 2. 키에 대한 TTL 조회 메서드 추가
    public Duration getTtl(String prefix) {
        return ttlMap.getOrDefault(prefix, Duration.ZERO);
    }

    // 4. 키 파싱 메서드 추가 (필요한 경우)
    public Optional<String> extractMemberIdFromKey(String key) {
        String[] parts = key.split(RedisConstants.DELIMITER);
        if (parts.length >= 3) {
            return Optional.of(parts[2]);
        }
        return Optional.empty();
    }

    // 5. 복합 키 생성 메서드 추가 (필요한 경우)
    public String createCompositeKey(String... parts) {
        if (parts == null || parts.length == 0) {
            throw new IllegalArgumentException("Key parts cannot be null or empty");
        }
        return String.join(RedisConstants.DELIMITER, 
            RedisConstants.SERVICE_ID, 
            String.join(RedisConstants.DELIMITER, parts));
    }

    // Access Token 키 생성
    public String accessToken(String memberId) {
        return String.join(RedisConstants.DELIMITER, RedisConstants.SERVICE_ID, RedisConstants.Prefix.ACCESS_TOKEN, memberId);
    }

    // Refresh Token 키 생성
    public String refreshToken(String memberId) {
        return String.join(RedisConstants.DELIMITER, RedisConstants.SERVICE_ID, RedisConstants.Prefix.REFRESH_TOKEN, memberId);
    }

    // User Session 키 생성
    public String userSession(String memberId) {
        return String.join(RedisConstants.DELIMITER, RedisConstants.SERVICE_ID, RedisConstants.Prefix.USER_SESSION, memberId);
    }

    // Blacklist 키 생성
    public String blacklist(String token) {
        return String.join(RedisConstants.DELIMITER, RedisConstants.SERVICE_ID, RedisConstants.Prefix.BLACKLIST, token);
    }

    // Role 키 생성
    public String role(String roleType) {
        return String.join(RedisConstants.DELIMITER, RedisConstants.SERVICE_ID, RedisConstants.Prefix.ROLE, roleType);
    }

    // 키 패턴 생성 (검색용)
    public String pattern(String type) {
        return String.join(RedisConstants.DELIMITER, RedisConstants.SERVICE_ID, type, "*");
    }
}
