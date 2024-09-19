package com.lolclone.authentication_management_server.dto;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.NotNull;

public record LogoutRequest(
    @NotNull @UUID String refreshToken
) {
    
}
