package com.lolclone.authenticationmanagementinfra.oauth2.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.lolclone.authenticationmanagementdomain.exception.OAuth2TokenException;
import com.lolclone.authenticationmanagementinfra.oauth2.dto.KakaoOAuth2TokenResponse;

import lombok.Getter;

@Component
@Getter
public class KakaoOAuth2TokenClient {
    private final RestTemplate restTemplate;
    private static final String URL = "https://kauth.kakao.com/oauth/token";
    private final String clientId;
    private final String grantType;
    private final String redirectUri;
    private final String clientSecret;

    public KakaoOAuth2TokenClient(
            @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}") String grantType,
            @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri,
            @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret,
            RestTemplateBuilder restTemplateBuilder
    ) {
        this.clientId = clientId;
        this.grantType = grantType;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.restTemplate = restTemplateBuilder
                .errorHandler(new KakaoOAuth2AccessTokenErrorHandler())
                .build();
    }

    public String getIdToken(String code) {
        KakaoOAuth2TokenResponse response = restTemplate.postForEntity(URL, getHeader(code), KakaoOAuth2TokenResponse.class).getBody();
        if(response == null) throw new OAuth2TokenException();
        return response.idToken();
    }

    private HttpHeaders getHeader(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("grant_type", grantType);
        headers.set("client_id", clientId);
        headers.set("redirect_uri", redirectUri);
        headers.set("client_secret", clientSecret);
        headers.set("code", code);
        return headers;
    }
}