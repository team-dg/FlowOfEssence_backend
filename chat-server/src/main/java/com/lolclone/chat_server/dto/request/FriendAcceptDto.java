package com.lolclone.chat_server.dto.request;

import jakarta.validation.constraints.NotNull;

public record FriendAcceptDto(
    @NotNull(message = "수신자 ID는 필수입니다")
    Long receiverId,
    
    @NotNull(message = "요청자 ID는 필수입니다")
    Long requesterId
) {
} 