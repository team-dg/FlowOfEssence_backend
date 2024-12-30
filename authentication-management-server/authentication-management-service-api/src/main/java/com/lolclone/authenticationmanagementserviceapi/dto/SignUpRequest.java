package com.lolclone.authenticationmanagementserviceapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
    @NotNull(message = "사용자 이름은 필수입니다")
    @Size(min = 3, max = 20, message = "사용자 이름은 3자 이상 20자 이하여야 합니다")
    String username,

    @NotNull(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하여야 합니다")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).*$", message = "비밀번호는 숫자, 영문, 특수문자를 포함해야 합니다")
    String password,

    @Email(message = "이메일 형식이 올바르지 않습니다")
    @NotNull(message = "이메일은 필수입니다")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", message = "유효한 이메일 주소를 입력해주세요")
    String email,

    String nickname
) {
    
}
