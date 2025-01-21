package com.lolclone.chatdomain.domain.gameinvite;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameInviteMetadata {
    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    private GameInviteMetadata(LocalDateTime processedAt, LocalDateTime expiresAt) {
        this.processedAt = processedAt;
        this.expiresAt = expiresAt;
    }

    public static GameInviteMetadata init() {
        return new GameInviteMetadata(
            null,
            LocalDateTime.now().plusMinutes(5) // 5분 후 만료
        );
    }

    public GameInviteMetadata updateProcessedTime() {
        return new GameInviteMetadata(
            LocalDateTime.now(),
            this.expiresAt
        );
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isProcessed() {
        return processedAt != null;
    }

    public Duration getTimeUntilExpiration() {
        return Duration.between(LocalDateTime.now(), expiresAt);
    }
}
