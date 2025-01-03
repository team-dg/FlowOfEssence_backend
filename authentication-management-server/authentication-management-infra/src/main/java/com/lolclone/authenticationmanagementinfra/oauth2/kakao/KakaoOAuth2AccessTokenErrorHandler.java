package com.lolclone.authenticationmanagementinfra.oauth2.kakao;

import java.io.IOException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;

import com.lolclone.authenticationmanagementdomain.exception.ExceptionType;
import com.lolclone.authenticationmanagementdomain.exception.OAuth2ServerException;
import com.lolclone.authenticationmanagementdomain.exception.OAuth2TokenException;
import com.lolclone.authenticationmanagementinfra.oauth2.dto.KakaoOAuth2ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KakaoOAuth2AccessTokenErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        try {
            super.handleError(response);
        } catch (HttpStatusCodeException e) {
            HttpStatusCode statusCode = response.getStatusCode();
            handle4xxError(statusCode, e);
            handle5xxError(statusCode);
        }
        throw new OAuth2ServerException(ExceptionType.OAUTH2_SERVER_ERROR);
    }

    private void handle4xxError(HttpStatusCode code, HttpStatusCodeException e) {
        if (code.is4xxClientError()) {
            KakaoOAuth2ErrorResponse response = e.getResponseBodyAs(KakaoOAuth2ErrorResponse.class);
            handleErrorCode(response);
        }
    }

    private void handleErrorCode(KakaoOAuth2ErrorResponse response) {
        handleKOE320Error(response);
        log.warn("OAuth2 error response: {}", response);
        throw new OAuth2TokenException();
    }

    private void handleKOE320Error(KakaoOAuth2ErrorResponse response) {
        if(response != null && response.isErrorCodeKOE320()) {
            throw new OAuth2TokenException();
        }
    }

    private void handle5xxError(HttpStatusCode statusCode) {
        if (statusCode.is5xxServerError()) {
            throw new OAuth2ServerException(ExceptionType.OAUTH2_PROVIDER_NOT_RESPONSE);
        }
    }
}
