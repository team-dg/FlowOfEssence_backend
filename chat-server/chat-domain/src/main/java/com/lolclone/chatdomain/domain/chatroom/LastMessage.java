package com.lolclone.chatdomain.domain.chatroom;

import java.time.LocalDateTime;

import com.lolclone.chatdomain.domain.message.Message;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LastMessage {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id")
    private Message message;

    @Column(name = "last_message_sent_at")
    private LocalDateTime sentAt;

    private LastMessage(Message message) {
        this.message = message;
        this.sentAt = LocalDateTime.now();
    }

    public static LastMessage empty() {
        return new LastMessage(null);
    }

    public static LastMessage from(Message message) {
        return new LastMessage(message);
    }

    public boolean hasMessage() {
        return message != null;
    }
}
