package com.lolclone.chat_server.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeleteFolderRequest(
    @NotNull(message = "사용자 ID는 필수입니다")
    Long userId,
    
    @NotNull(message = "폴더 ID는 필수입니다")
    Long folderId
) {
    
} 