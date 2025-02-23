package com.lolclone.authenticationmanagementinfra.infrastructure;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Properties;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2UserUnlink;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Properties.ProviderProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NaverOAuth2UserUnlink implements OAuth2UserUnlink{
    private final OAuth2Properties oauth2Properties;
    private final RestTemplate restTemplate;
    private static final String PROVIDER_NAME = "naver";
    private static final String SERVICE_PROVIDER = "service_provider";
    private static final String NAVER = "NAVER";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String GRANT_TYPE = "grant_type";
    private static final String DELETE = "delete";
    private static final String ACCESS_TOKEN = "access_token";

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Override
    public void unlink(UUID memberId, String accessToken) {
        ProviderProperties naver = oauth2Properties.getProviderProperties(PROVIDER_NAME);
        String unlinkUrl = naver.getBaseUrl() + naver.getTokenUrl();
        HttpEntity<MultiValueMap<String, String>> httpEntity = createHttpEntity(accessToken);

        NaverUnlinkResponse response = restTemplate.exchange(unlinkUrl, HttpMethod.POST, httpEntity, NaverUnlinkResponse.class).getBody();
        if (response == null || !response.getResult().equals("success")) {
            throw new RuntimeException("Failed to unlink Naver account"); // 추후 사용자 정의 예외로 수정
        }
    }

    private HttpEntity<MultiValueMap<String, String>> createHttpEntity(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(SERVICE_PROVIDER, NAVER);
        body.add(GRANT_TYPE, DELETE);
        body.add(CLIENT_ID, clientId);
        body.add(CLIENT_SECRET, clientSecret);
        body.add(ACCESS_TOKEN, accessToken);

        return new HttpEntity<>(body, headers);
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.NAVER;
    }

    @Getter
    @RequiredArgsConstructor
    public static class NaverUnlinkResponse {
        @JsonProperty("access_token")
        private final String accessToken;
        private final String result;
    }
}