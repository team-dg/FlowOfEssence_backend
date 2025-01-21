package com.lolclone.chatdomain.domain.chatparticipant;

import java.time.LocalDateTime;
import java.util.List;

import com.lolclone.chatdomain.domain.message.Message;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LastReadMessage {
    @Column(name = "last_read_message_id")
    private Long messageId;

    @Column(name = "last_read_at")
    private LocalDateTime readAt;

    private LastReadMessage(Message message) {
        this.messageId = message.getId();
        this.readAt = LocalDateTime.now();
    }

    public static LastReadMessage init() {
        return new LastReadMessage();
    }

    public static LastReadMessage of(Message message) {
        return new LastReadMessage(message);
    }

    public boolean isOlderThan(Message message) {
        return messageId == null || messageId < message.getId();
    }

    public int calculateUnreadCount(List<Message> messages) {
        if (messageId == null) return messages.size();
        return (int) messages.stream()
            .filter(m -> m.getId() > messageId)
            .count();
    }
}
