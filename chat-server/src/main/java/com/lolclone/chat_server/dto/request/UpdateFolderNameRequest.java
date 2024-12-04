package com.lolclone.chat_server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateFolderNameRequest(
    @NotNull(message = "사용자 ID는 필수입니다")
    Long userId,
    
    @NotBlank(message = "폴더 이름은 비어있을 수 없습니다") 
    String newName,
    
    @NotNull(message = "폴더 ID는 필수입니다")
    Long folderId
) {
    
} 