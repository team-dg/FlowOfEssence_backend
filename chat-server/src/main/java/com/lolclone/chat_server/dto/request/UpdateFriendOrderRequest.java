package com.lolclone.chat_server.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateFriendOrderRequest(
    @NotNull(message = "친구 ID는 필수입니다")
    UUID friendId,
    
    @Min(value = 0, message = "순서는 0 이상이어야 합니다") 
    int newOrder,
    
    @NotNull(message = "폴더 ID는 필수입니다")
    UUID folderId
) {

} 