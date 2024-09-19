package com.lolclone.authentication_management_server.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginResult(
    Long userId,
    String nickname,
    String profileImageUrl,
    UUID refreshToken,
    LocalDateTime refreshTokenExpiredAt
) {
    
}
