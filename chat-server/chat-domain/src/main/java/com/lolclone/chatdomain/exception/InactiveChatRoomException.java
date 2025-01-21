package com.lolclone.chatdomain.exception;

import com.lolclone.chatdomain.domain.chatroom.ChatRoomId;

public class InactiveChatRoomException extends RuntimeException {
    private final ChatRoomId chatRoomId;
    
    public InactiveChatRoomException(ChatRoomId chatRoomId) {
        super(String.format("채팅방이 비활성화되었습니다. 채팅방 ID: %s", chatRoomId));
        this.chatRoomId = chatRoomId;
    }

    public ChatRoomId getChatRoomId() {
        return chatRoomId;
    }
}
