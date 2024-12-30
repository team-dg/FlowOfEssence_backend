package com.lolclone.authenticationmanagementserviceapi.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
    @NotNull(message = "사용자 이름은 필수입니다")
    String username,
    @NotNull(message = "비밀번호는 필수입니다")
    String password
) {

}
