package com.lolclone.authenticationmanagementserviceapi.dto;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
    @NotNull(message = "리프레시 토큰은 필수입니다")
    @UUID(message = "유효한 UUID 형식이어야 합니다")
    String refreshToken
) {
    
}
