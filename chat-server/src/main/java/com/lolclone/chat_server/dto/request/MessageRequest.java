package com.lolclone.chat_server.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageRequest(
    @NotNull(message = "사용자 ID는 필수입니다")
    UUID userId,
    
    @NotNull(message = "수신자 ID는 필수입니다")
    UUID receiverId,
    
    @NotNull(message = "메시지 내용은 필수입니다")
    @Size(min = 1, max = 500, message = "메시지는 1자 이상 500자 이하여야 합니다")
    String message
) {
    
} 