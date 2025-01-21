package com.lolclone.chatserviceapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lolclone.chatdomain.domain.MemberStatus;
import com.lolclone.chatdomain.domain.chatparticipant.ChatParticipant;
import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
import com.lolclone.chatdomain.domain.friend.Friend;

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
    // public static FriendChatInfoDto of(Friend friend, ChatRoom chatRoom, ChatParticipant participant, int unreadMessageCount) {
    //     return FriendChatInfoDto.builder()
    //         .friendId(friend.getFriend().getId().getValue())
    //         .friendNickname(friend.getFriend().getNickname().getValue())
    //         .chatRoomId(chatRoom.getId().getValue())
    //         .lastMessage(chatRoom.getLastMessage() != null ? chatRoom.getLastMessage().getMessage().getContent().getValue() : null)
    //         .lastMessageTime(chatRoom.getLastMessage() != null ? chatRoom.getLastMessage().getMessage().getCreatedDate() : null)
    //         .isOnline(friend.getFriend().isOnline())
    //         .isMuted(participant != null && participant.isMuted())
    //         .unreadMessageCount(unreadMessageCount)
    //         .status(friend.getFriend().getStateInfo().getStatus())
    //         .build();
    // }
}

