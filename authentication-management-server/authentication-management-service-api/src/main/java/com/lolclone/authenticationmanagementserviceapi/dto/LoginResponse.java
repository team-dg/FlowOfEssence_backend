package com.lolclone.authenticationmanagementserviceapi.dto;

import java.util.UUID;

public record LoginResponse(
    TokenResponse accessToken,
    TokenResponse refreshToken,
    UUID userId,
    String nickname
) {
    
}
