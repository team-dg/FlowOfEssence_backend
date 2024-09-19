package com.lolclone.authentication_management_server.infrastructure.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoOAuth2TokenResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("id_token") String idToken
) {
}
