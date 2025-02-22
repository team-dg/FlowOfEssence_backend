package com.lolclone.chatdomain.repository.friend.query;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

public record ChatRoomInfoDto(
    UUID chatRoomId,
    String lastMessage,
    UUID lastMessageSenderId,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastMessageTime,
    boolean isMuted,
    int unreadMessageCount
) {
    @QueryProjection
    public ChatRoomInfoDto(UUID chatRoomId, String lastMessage, UUID lastMessageSenderId, LocalDateTime lastMessageTime, boolean isMuted, int unreadMessageCount) {
        this.chatRoomId = chatRoomId;
        this.lastMessage = lastMessage;
        this.lastMessageSenderId = lastMessageSenderId;
        this.lastMessageTime = lastMessageTime;
        this.isMuted = isMuted;
        this.unreadMessageCount = unreadMessageCount;
    }

}
