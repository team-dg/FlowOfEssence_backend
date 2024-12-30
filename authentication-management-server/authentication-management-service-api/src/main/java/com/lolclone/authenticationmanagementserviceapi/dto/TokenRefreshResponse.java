package com.lolclone.authenticationmanagementserviceapi.dto;

public record TokenRefreshResponse(
    TokenResponse accessToken,
    TokenResponse refreshToken
) {
    
}
