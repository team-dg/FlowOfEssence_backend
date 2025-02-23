package com.lolclone.authenticationmanagementinfra.infrastructure;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Properties;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2UserUnlink;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Properties.ProviderProperties;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.OAuth2UnlinkException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2UserUnlink implements OAuth2UserUnlink{
    private final OAuth2Properties oauth2Properties;
    private final RestTemplate restTemplate;
    private static final String PROVIDER_NAME = "google";
    private static final String GOOGLE_UNLINK_REQUEST_BODY = "token=%s";

    @Override
    public void unlink(UUID memberId, String accessToken) {
        ProviderProperties google = oauth2Properties.getProviderProperties(PROVIDER_NAME);
        String unlinkUrl = google.getBaseUrl() + google.getRevokeUrl();
        HttpEntity<String> httpEntity = createHttpEntity(accessToken);

        try {
            restTemplate.exchange(unlinkUrl, HttpMethod.POST, httpEntity, String.class);
        } catch(RestClientException e) {
            throw new OAuth2UnlinkException(ExceptionType.OAUTH2_UNLINK_FAILED, PROVIDER_NAME);
        }
    }

    private HttpEntity<String> createHttpEntity(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = String.format(GOOGLE_UNLINK_REQUEST_BODY, accessToken);
        return new HttpEntity<>(body, headers);
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
    }
}