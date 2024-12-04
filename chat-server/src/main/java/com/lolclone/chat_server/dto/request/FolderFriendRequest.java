package com.lolclone.chat_server.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record FolderFriendRequest(
    @NotNull(message = "폴더 ID는 필수입니다")
    Long folderId,
    
    @NotNull(message = "친구 ID는 필수입니다")
    Long friendId,
    
    @NotNull(message = "작업 유형은 필수입니다")
    @Pattern(regexp = "^(add|remove)$", message = "작업 유형은 'add' 또는 'remove'여야 합니다")
    String action
) {} 