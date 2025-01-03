package com.lolclone.chat_server.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record GetFriendInfoRequest(
    @NotNull(message = "사용자 ID는 필수입니다")
    UUID userId,
    
    @NotNull(message = "조회할 친구 ID는 필수입니다")
    UUID friendId
) {

} 