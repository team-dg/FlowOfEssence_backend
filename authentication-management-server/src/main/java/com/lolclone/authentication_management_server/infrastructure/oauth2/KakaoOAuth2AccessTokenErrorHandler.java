package com.lolclone.authentication_management_server.infrastructure.oauth2;

import com.lolclone.authentication_management_server.infrastructure.oauth2.dto.KakaoOAuth2ErrorResponse;
import com.lolclone.common_module.commonexception.BadRequestException;
import com.lolclone.common_module.commonexception.InternalServerException;
import com.lolclone.common_module.exception.domain.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

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
        throw new InternalServerException(ExceptionType.SERVER_ERROR);
    }

    private void handle4xxError(HttpStatusCode code, HttpStatusCodeException e) {
        if (code.is4xxClientError()) {
            KakaoOAuth2ErrorResponse response = e.getResponseBodyAs(KakaoOAuth2ErrorResponse.class);
            handleErrorCode(response);
        }
    }

    private void handleErrorCode(KakaoOAuth2ErrorResponse response) {
        handleKOE320Error(response);
        log.warn("{}", response);
        throw new BadRequestException(ExceptionType.OAUTH2_INVALID_REQUEST);
    }

    private void handleKOE320Error(KakaoOAuth2ErrorResponse response) {
        if(response != null && response.isErrorCodeKOE320()) {
            throw new BadRequestException(ExceptionType.OAUTH2_INVALID_CODE);
        }
    }

    private void handle5xxError(HttpStatusCode statusCode) {
        if (statusCode.is5xxServerError()) {
            throw new InternalServerException(ExceptionType.OAUTH2_PROVIDER_NOT_RESPONSE);
        }
    }
}
