package com.lolclone.chatdomain.exception;

import com.lolclone.chatdomain.domain.chatroom.ChatRoomId;

public class TooManyParticipantsException extends RuntimeException {
    private final ChatRoomId chatRoomId;

    public TooManyParticipantsException(ChatRoomId chatRoomId) {
        super(String.format("채팅방 참여자가 너무 많습니다. 채팅방 ID: %s", chatRoomId));
        this.chatRoomId = chatRoomId;
    }

    public ChatRoomId getChatRoomId() {
        return chatRoomId;
    }
}
