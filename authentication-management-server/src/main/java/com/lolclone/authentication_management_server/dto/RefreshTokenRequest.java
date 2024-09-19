package com.lolclone.authentication_management_server.dto;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
    @NotNull @UUID String refreshToken
) {
    
}
