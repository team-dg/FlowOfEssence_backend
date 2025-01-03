package com.lolclone.chat_server.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record DeleteFolderRequest(
    @NotNull(message = "사용자 ID는 필수입니다")
    UUID userId,
    
    @NotNull(message = "폴더 ID는 필수입니다")
    UUID folderId
) {
    
} 