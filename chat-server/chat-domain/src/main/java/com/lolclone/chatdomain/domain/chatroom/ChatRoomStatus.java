package com.lolclone.chatdomain.domain.chatroom;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomStatus {
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;

    private ChatRoomStatus(Status status) {
        this.status = status;
        this.deactivatedAt = status == Status.DEACTIVATED_BY_USER || status == Status.DEACTIVATED_BY_ADMIN ? LocalDateTime.now() : null;
    }

    public static ChatRoomStatus active() {
        return new ChatRoomStatus(Status.ACTIVE);
    }

    public ChatRoomStatus deactivate() {
        return new ChatRoomStatus(Status.DEACTIVATED_BY_USER);
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    public enum Status {
        ACTIVE,
        DEACTIVATED_BY_USER,
        DEACTIVATED_BY_ADMIN
    }
}
