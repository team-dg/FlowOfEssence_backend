package com.lolclone.authentication_management_server.dto;

public record TokenRefreshResponse(
    TokenResponse accessToken,
    TokenResponse refreshToken
) {
    
}
