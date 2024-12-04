package com.lolclone.chat_server.dto.response;

import java.time.LocalDateTime;

import com.lolclone.chat_server.domain.Friend;
import com.lolclone.chat_server.domain.User;

public record FriendResponseDto(
    Long friendId,
    String friendName,
    String tag,
    String status,
    String gameInfo,
    LocalDateTime lastActive,
    String memo,
    boolean isBlocked
) {
    public static FriendResponseDto from(Friend friendship) {
        User friend = friendship.getFriend();
        return new FriendResponseDto(
            friend.getId(),
            friend.getNickname(),
            friend.getTag(),
            friend.getStatus().toString(),
            friend.getGameInfo(),
            friend.getUpdatedDate(),
            friendship.getMemo(),
            friendship.isBlocked()
        );
    }
}