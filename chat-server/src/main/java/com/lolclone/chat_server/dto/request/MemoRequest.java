package com.lolclone.chat_server.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemoRequest(
    @NotNull(message = "사용자 ID는 필수입니다")
    UUID userId,
    
    @NotNull(message = "친구 ID는 필수입니다") 
    UUID friendId,
    
    @Size(max = 500, message = "메모는 500자를 초과할 수 없습니다")
    String memo
) {

} 
