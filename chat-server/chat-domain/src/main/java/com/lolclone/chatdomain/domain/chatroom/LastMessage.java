package com.lolclone.chatdomain.domain.chatroom;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LastMessage {

    private Long lastMessageId;

    @Column(name = "last_message_sent_at")
    private LocalDateTime sentAt;

    private LastMessage(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
        this.sentAt = LocalDateTime.now();
    }

    public static LastMessage empty() {
        return new LastMessage(null);
    }

    public static LastMessage from(Long lastMessageId) {
        return new LastMessage(lastMessageId);
    }

    public boolean hasMessage() {
        return lastMessageId != null;
    }
}
