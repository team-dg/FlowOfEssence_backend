package com.lolclone.authentication_management_server.dto;

import com.lolclone.common_module.util.Validator;

public record SignUpRequest(
    String username,
    String password,
    String email,
    String nickname
) {
    public static void from(SignUpRequest signUpRequest) {
        Validator.notBlank(signUpRequest.username(), "username");
        Validator.minLength(signUpRequest.username(), 4, "username");
        Validator.maxLength(signUpRequest.username(), 20, "username");

        Validator.notBlank(signUpRequest.password(), "password");
        Validator.minLength(signUpRequest.password(), 8, "password");

        Validator.notBlank(signUpRequest.email(), "email");
        Validator.email(signUpRequest.email(), "email");

        Validator.notBlank(signUpRequest.nickname(), "nickname");
    }
}
