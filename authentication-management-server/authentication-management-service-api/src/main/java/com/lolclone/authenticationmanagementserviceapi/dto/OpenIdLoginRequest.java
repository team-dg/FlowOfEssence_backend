package com.lolclone.authenticationmanagementserviceapi.dto;

import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OpenIdLoginRequest(
    @NotNull(message = "소셜 타입은 필수입니다")
    SocialType socialType,
    
    @NotBlank(message = "ID 토큰은 필수이며 공백일 수 없습니다")
    String idToken
) {
    
}
