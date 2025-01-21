package com.lolclone.chatdomain.domain.chatroom;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomStatus {
    @Column(name = "active")
    private boolean active;

    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;

    private ChatRoomStatus(boolean active) {
        this.active = active;
        this.deactivatedAt = active ? null : LocalDateTime.now();
    }

    public static ChatRoomStatus active() {
        return new ChatRoomStatus(true);
    }

    public ChatRoomStatus deactivate() {
        return new ChatRoomStatus(false);
    }

    public boolean isActive() {
        return active;
    }
}
