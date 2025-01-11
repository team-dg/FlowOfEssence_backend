package com.lolclone.authenticationmanagementserviceapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginResult(
    UUID userId,
    String nickname,
    UUID refreshToken,
    LocalDateTime refreshTokenExpiredAt
) {
    
}
