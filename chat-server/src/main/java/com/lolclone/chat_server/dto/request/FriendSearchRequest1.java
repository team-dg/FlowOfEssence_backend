package com.lolclone.chat_server.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FriendSearchRequest1(
    @NotNull(message = "사용자 ID는 필수입니다")
    UUID userId,
    @NotBlank(message = "닉네임은 필수입니다")
    String nickname,
    @NotBlank(message = "태그는 필수입니다")
    String tag
) {
    
}
