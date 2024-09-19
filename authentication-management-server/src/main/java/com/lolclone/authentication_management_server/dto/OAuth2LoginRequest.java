package com.lolclone.authentication_management_server.dto;

import com.lolclone.database_server.authenticationserver.domain.SocialType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OAuth2LoginRequest(
    @NotNull SocialType socialType,
    @NotBlank String code
) {
    
}
