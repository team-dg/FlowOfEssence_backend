package com.lolclone.authenticationmanagementserviceapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TokenRefreshResult(
    UUID userId,
    UUID refreshToken,
    LocalDateTime expiredAt
) {
    
}
