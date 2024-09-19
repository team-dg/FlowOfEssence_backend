package com.lolclone.authentication_management_server.dto;

public record LoginResponse(
    String nickname,
    String profileImageUrl,
    TokenResponse accessToken,
    TokenResponse refreshToken
) {
    
}
