package com.lolclone.chat_server.dto.response;

import com.lolclone.chat_server.domain.FriendRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public record FriendRequestResponseDto(
    UUID requesterId,
    String requesterNickname,
    LocalDateTime requestDate
) {
    public static FriendRequestResponseDto from(FriendRequest request) {
        return new FriendRequestResponseDto(
            request.getSenderId(),
            request.getSender().getNickname(),
            request.getCreatedDate()
        );
    }
} 