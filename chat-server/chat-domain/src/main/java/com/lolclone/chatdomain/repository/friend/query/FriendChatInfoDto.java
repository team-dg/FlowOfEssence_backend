package com.lolclone.chatdomain.repository.friend.query;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.lolclone.chatdomain.domain.MemberStatus;
import com.lolclone.chatdomain.domain.chatparticipant.ChatParticipant;
import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
import com.lolclone.chatdomain.domain.friend.Friend;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.domain.message.Message;

import lombok.Builder;

@Builder
public record FriendChatInfoDto(
    UUID friendId,
    String friendNickname,

    MemberStatus status,
    String gameInfo, // 게임 중일 경우 게임 정보 (예: "솔로랭크 게임 중")
    LocalDateTime lastActiveTime,
    boolean isOnline,

    //채팅방 정보
    UUID chatRoomId,
    String lastMessage,
    UUID lastMessageSenderId,
    LocalDateTime lastMessageTime,

    //채팅 알림 설정
    boolean isMuted,
    int unreadMessageCount,

    //친구 관계 정보
    String memo,
    Set<String> tags,
    boolean isBlocked
) {
    public static FriendChatInfoDto of(
        Friend friend, 
        ChatRoom chatRoom, 
        ChatParticipant participant,
        int unreadMessageCount
    ) {
        Member friendMember = friend.getFriend();
        Message lastMessage = chatRoom != null ? chatRoom.getLastMessage().getMessage() : null;
        
        return FriendChatInfoDto.builder()
                // 친구 기본 정보
                .friendId(friendMember.getId().getValue())
                .friendNickname(friendMember.getNickname())

                // 친구 상태 정보
                .status(friendMember.getStateInfo().getStatus())
                .gameInfo(friendMember.getGameStatusDisplay())
                .lastActiveTime(friendMember.getLastActiveTime())

                // 채팅방 정보
                .chatRoomId(chatRoom != null ? chatRoom.getId().getValue() : null)
                .lastMessage(lastMessage != null ? lastMessage.getContent().getValue() : null)
                .lastMessageSenderId(lastMessage != null ? lastMessage.getSender().getId().getValue() : null)
                .lastMessageTime(lastMessage != null ? lastMessage.getCreatedDate() : null)

                // 채팅 알림 설정
                .isMuted(participant != null && participant.isMuted())
                .unreadMessageCount(unreadMessageCount)

                // 친구 관계 정보
                .memo(friend.getMemo())
                .tags(friendMember.getTags())
                .isBlocked(friend.isBlocked())
                .build();
    }
}
