package com.lolclone.authenticationmanagementinfra.utils;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.lolclone.authenticationmanagementinfra.service.TokenProviderTemplate;
import com.lolclone.commonmodule.domain.SessionStatus;
import com.lolclone.commonmodule.domain.UserSession;
import com.lolclone.commonmodule.utils.RedisKeyGenerator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisKeyGenerator keyGenerator;
    private final TokenProviderTemplate tokenProviderTemplate;

    /**
     * 사용자 세션 정보를 저장합니다.
     * @param userSession 사용자 세션 객체
     * @param ttlMillis 세션 유효 시간(밀리초)
     */
    public void saveUserSession(UserSession userSession, long ttlMillis) {
        String sessionKey = keyGenerator.userSession(userSession.getMemberId().toString());
        redisTemplate.opsForValue().set(sessionKey, userSession, Duration.ofMillis(ttlMillis));
    }

    public UserSession getUserSession(String memberId) {
        String sessionKey = keyGenerator.userSession(memberId);
        return (UserSession) redisTemplate.opsForValue().get(sessionKey);
    }

    public boolean invalidateUserSession(String memberId) {
        String sessionKey = keyGenerator.userSession(memberId);
        return Boolean.TRUE.equals(redisTemplate.delete(sessionKey));
    }

    public UserSession updateSessionStatus(UUID memberId, SessionStatus status) {
        UserSession session = getUserSession(memberId.toString());
        if (session != null) {
            session.updateStatus(status);
            saveUserSession(session, tokenProviderTemplate.getRemainTime(session.getRefreshToken()));
        }
        return session;
    }

    // -----------------------------------------------------

    /**
     * 블랙리스트 토큰 설정
     */
    public void setBlacklistToken(String accessToken, UUID memberId, long expirationTimeMillis) {
        String key = keyGenerator.blacklist(accessToken);
        redisTemplate.opsForValue().set(key, memberId.toString(), Duration.ofMillis(expirationTimeMillis));
    }

    public boolean isBlacklistToken(String accessToken) {
        String key = keyGenerator.blacklist(accessToken);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public String getBlacklistedTokenOwner(String token) {
        String key = keyGenerator.blacklist(token);
        return (String) redisTemplate.opsForValue().get(key);
    }

    // -----------------------------------------------------------

    // Hash 작업 추가
    public void setHashField(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public Object getHashField(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public Map<Object, Object> getAllHashFields(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    // ------------------------------------------------------------

    public void addUserToRole(String role, String memberId) {
        String key = keyGenerator.role(role);
        redisTemplate.opsForSet().add(key, memberId);
    }

    public Set<Object> getUsersByRole(String role) {
        String key = keyGenerator.role(role);
        return redisTemplate.opsForSet().members(key);
    }

    public boolean isUserInRole(String role, String memberId) {
        String key = keyGenerator.role(role);
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, memberId));
    }

    public void removeUserFromRole(String role, String memberId) {
        String key = keyGenerator.role(role);
        redisTemplate.opsForSet().remove(key, memberId);
    }

    // ------------------------------------------------------------


    public void set(String key, Object value, long timeoutSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutSeconds));
    }
    
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
    
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
