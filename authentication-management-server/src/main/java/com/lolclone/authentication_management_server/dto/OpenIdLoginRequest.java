package com.lolclone.authentication_management_server.dto;

import com.lolclone.database_server.authenticationserver.domain.SocialType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OpenIdLoginRequest(
    @NotNull SocialType socialType,
    @NotBlank String idToken
) {
    
}
