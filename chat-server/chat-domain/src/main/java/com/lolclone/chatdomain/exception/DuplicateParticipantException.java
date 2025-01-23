package com.lolclone.chatdomain.exception;

import java.util.UUID;

public class DuplicateParticipantException extends RuntimeException {
    private final UUID userId;
    private final UUID chatRoomId;

    public DuplicateParticipantException(UUID userId, UUID chatRoomId) {
        super(String.format("채팅방 참여자가 이미 존재합니다. 사용자 ID: %s, 채팅방 ID: %s", userId, chatRoomId));
        this.userId = userId;
        this.chatRoomId = chatRoomId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChatRoomId() {
        return chatRoomId;
    }
}
