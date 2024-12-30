package com.lolclone.authenticationmanagementinfra.oauth2.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoOAuth2ErrorResponse(
        String error,
        @JsonProperty("error_description") String errorDescription,
        @JsonProperty("error_code") String errorCode
) {
    public boolean isErrorCodeKOE320() {
        return Objects.equals(errorCode, "KOE320");
    }
}
