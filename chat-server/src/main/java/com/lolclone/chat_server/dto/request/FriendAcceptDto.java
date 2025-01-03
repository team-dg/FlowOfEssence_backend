package com.lolclone.chat_server.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record FriendAcceptDto(
    @NotNull(message = "수신자 ID는 필수입니다")
    UUID receiverId,
    
    @NotNull(message = "요청자 ID는 필수입니다")
    UUID requesterId
) {
    
} 