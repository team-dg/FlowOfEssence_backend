package com.lolclone.chat_server.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeleteFriendRequestDto(
    @NotNull(message = "사용자 ID는 필수입니다")
    Long userId,
    
    @NotNull(message = "삭제할 친구 ID는 필수입니다")
    Long friendId
) {

} 
