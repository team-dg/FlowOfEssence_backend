package com.lolclone.authentication_management_server.infrastructure.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record KakaoOAuth2ErrorResponse(
        String error,
        @JsonProperty("error_description") String errorDescription,
        @JsonProperty("error_code") String errorCode
) {
    public boolean isErrorCodeKOE320() {
        return Objects.equals(errorCode, "KOE320");
    }
}
