package com.lolclone.chat_server.dto.request;

import jakarta.validation.constraints.NotNull;

public record FriendRequestDto(
    @NotNull(message = "발신자 ID는 필수입니다")
    Long senderId,
    
    @NotNull(message = "수신자 닉네임은 필수입니다")
    String receiverNickname
) {
} 