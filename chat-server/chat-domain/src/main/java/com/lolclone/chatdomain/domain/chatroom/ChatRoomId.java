package com.lolclone.chatdomain.domain.chatroom;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomId implements Serializable {
    @Column(name = "chat_room_id")
    private UUID value;

    private ChatRoomId(UUID value) {
        this.value = value;
    }

    public static ChatRoomId newId() {
        return new ChatRoomId(UUID.randomUUID());
    }

    public static ChatRoomId of(UUID value) {
        return new ChatRoomId(value);
    }

    public UUID getValue() {
        return value;
    }
}
