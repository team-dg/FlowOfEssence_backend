package com.lolclone.chatdomain.exception;

import com.lolclone.chatdomain.domain.chatroom.ChatRoomId;
import com.lolclone.chatdomain.domain.member.MemberId;

public class UnauthorizedParticipantException extends RuntimeException {
    private final MemberId userId;
    private final ChatRoomId chatRoomId;

    public UnauthorizedParticipantException(MemberId userId, ChatRoomId chatRoomId) {
        super(String.format("채팅방 참여자가 존재하지 않습니다. 사용자 ID: %s, 채팅방 ID: %s", userId, chatRoomId));
        this.userId = userId;
        this.chatRoomId = chatRoomId;
    }

    public MemberId getUserId() {
        return userId;
    }

    public ChatRoomId getChatRoomId() {
        return chatRoomId;
    }
}
