package com.lolclone.authenticationmanagementserviceapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginResult(
    UUID userId,
    UUID refreshToken,
    LocalDateTime refreshTokenExpiredAt
) {
    
}
