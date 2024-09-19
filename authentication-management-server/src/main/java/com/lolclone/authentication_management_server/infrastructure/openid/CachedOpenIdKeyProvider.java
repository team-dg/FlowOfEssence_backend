package com.lolclone.authentication_management_server.infrastructure.openid;

import io.jsonwebtoken.security.Jwk;
import io.jsonwebtoken.security.JwkSet;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
@Component
public class CachedOpenIdKeyProvider {
    private final Map<String, Key> cache = new ConcurrentHashMap<>();

    @Nullable
    public Key provide(String kid, Supplier<JwkSet> fallback) {
        return cache.computeIfAbsent(kid, k -> {
            log.info("kid에 대한 OpenId Key를 찾지 못해 Key 목록 조회를 시도합니다. kid={}", kid);
            try {
                // JwkSet을 가져옵니다.
                JwkSet jwkSet = fallback.get();
                // JwkSet의 모든 Jwk를 확인하고, 해당 kid에 맞는 key를 찾습니다.
                for (Jwk jwk : jwkSet) {
                    if (jwk.getId().equals(kid)) {
                        log.info("kid에 해당하는 key를 찾았습니다. kid={}", kid);
                        return jwk.toKey();
                    }
                }
                log.warn("OpenId의 kid에 대한 Key를 찾을 수 없습니다. kid={}", kid);
            } catch (Exception e) {
                log.error("JwkSet을 가져오는 중 예외가 발생했습니다. kid={}", kid, e);
            }
            return null;
        });
    }
}
