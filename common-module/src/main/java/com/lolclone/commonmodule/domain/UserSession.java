package com.lolclone.commonmodule.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID memberId;

    private String accessToken;

    private String refreshToken;

    private Instant lastAccessTime;

    private SessionStatus status;

    @Builder
    public UserSession(UUID memberId, String accessToken, String refreshToken) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.lastAccessTime = Instant.now();
        this.status = SessionStatus.ACTIVE;
    }

    public void updateStatus(SessionStatus status) {
        this.status = status;
    }

    public void updateLastAccessTime() {
        this.lastAccessTime = Instant.now();
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static UserSession createNew(UUID memberId, String accessToken, String refreshToken) {
        return UserSession.builder()
            .memberId(memberId)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
