package com.lolclone.authentication_management_server.dto;

import java.time.LocalDateTime;

public record TokenResponse(
    String token,
    LocalDateTime expiredAt
) {
    
}
