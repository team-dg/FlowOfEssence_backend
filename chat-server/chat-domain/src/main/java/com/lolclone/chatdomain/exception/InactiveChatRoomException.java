package com.lolclone.chatdomain.exception;

import java.util.UUID;

public class InactiveChatRoomException extends RuntimeException {
    private final UUID chatRoomId;
    
    public InactiveChatRoomException(UUID chatRoomId) {
        super(String.format("채팅방이 비활성화되었습니다. 채팅방 ID: %s", chatRoomId));
        this.chatRoomId = chatRoomId;
    }

    public UUID getChatRoomId() {
        return chatRoomId;
    }
}
