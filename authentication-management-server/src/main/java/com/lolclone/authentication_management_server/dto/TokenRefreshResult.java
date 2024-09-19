package com.lolclone.authentication_management_server.dto;

import java.time.LocalDateTime;

public record TokenRefreshResult(
    Long userId,
    String token,
    LocalDateTime expiredAt
) {
    
}
