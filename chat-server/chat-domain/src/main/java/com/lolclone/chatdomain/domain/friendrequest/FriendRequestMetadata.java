package com.lolclone.chatdomain.domain.friendrequest;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequestMetadata {
    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    private FriendRequestMetadata(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public static FriendRequestMetadata init() {
        return new FriendRequestMetadata(null);
    }

    public FriendRequestMetadata updateProcessedTime() {
        return new FriendRequestMetadata(LocalDateTime.now());
    }

    public boolean isProcessed() {
        return processedAt != null;
    }
}
