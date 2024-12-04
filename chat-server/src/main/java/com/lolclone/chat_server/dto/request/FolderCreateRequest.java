package com.lolclone.chat_server.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FolderCreateRequest(
    @NotNull(message = "사용자 ID는 필수입니다")
    Long userId,
    
    @NotNull(message = "폴더 이름은 필수입니다")
    @Size(min = 1, max = 50, message = "폴더 이름은 1자 이상 50자 이하여야 합니다")
    String folderName
) {} 