package com.lolclone.authenticationmanagementserviceapi.dto;

import java.time.LocalDateTime;

public record TokenResponse(
    String token,
    LocalDateTime expiredAt
) {
    
}
