package com.lolclone.chatdomain.domain.chatparticipant;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatParticipantId implements Serializable {
    @Column(name = "chat_room_ids")
    private UUID chatRoomId;

    @Column(name = "user_ids")
    private UUID userId;

    @Builder
    public ChatParticipantId(UUID chatRoomId, UUID userId) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
    }

    public static ChatParticipantId of(UUID chatRoomId, UUID userId) {
        return ChatParticipantId.builder()
            .chatRoomId(chatRoomId)
            .userId(userId)
            .build();
    }
}
