package com.lolclone.chatdomain.exception;

import java.util.UUID;

public class TooManyParticipantsException extends RuntimeException {
    private final UUID chatRoomId;

    public TooManyParticipantsException(UUID chatRoomId) {
        super(String.format("채팅방 참여자가 너무 많습니다. 채팅방 ID: %s", chatRoomId));
        this.chatRoomId = chatRoomId;
    }

    public UUID getChatRoomId() {
        return chatRoomId;
    }
}
