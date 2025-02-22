package com.lolclone.chatserviceapi.dto;

import java.util.UUID;

public record UserSearchResponseDto(
    UUID userId,
    String nickname
) {
    
}
