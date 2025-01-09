package com.lolclone.chatserviceapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lolclone.chatdomain.domain.ChatParticipant;
import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.Friend;
import com.lolclone.chatdomain.domain.MemberStatus;

import lombok.Builder;

@Builder
public record FriendChatInfoDto(
    UUID friendId,
    String friendNickname,
    UUID chatRoomId,
    String lastMessage,
    LocalDateTime lastMessageTime,
    boolean isOnline,
    boolean isMuted,
    int unreadMessageCount,
    MemberStatus status
) {
    public static FriendChatInfoDto of(Friend friend, ChatRoom chatRoom, ChatParticipant participant, int unreadMessageCount) {
        return FriendChatInfoDto.builder()
            .friendId(friend.getFriend().getId())
            .friendNickname(friend.getFriend().getNickname())
            .chatRoomId(chatRoom.getId())
            .lastMessage(chatRoom.getLastMessage() != null ? chatRoom.getLastMessage().getContent() : null)
            .lastMessageTime(chatRoom.getLastMessage() != null ? chatRoom.getLastMessage().getCreatedDate() : null)
            .isOnline(friend.getFriend().isOnline())
            .isMuted(participant != null && participant.isMuted())
            .unreadMessageCount(unreadMessageCount)
            .status(friend.getFriend().getStatus())
            .build();
    }
}
