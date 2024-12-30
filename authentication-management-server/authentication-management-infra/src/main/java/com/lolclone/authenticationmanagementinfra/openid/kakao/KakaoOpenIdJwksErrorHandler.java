package com.lolclone.authenticationmanagementinfra.openid.kakao;

import java.io.IOException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.lolclone.authenticationmanagementdomain.exception.ExceptionType;
import com.lolclone.authenticationmanagementdomain.exception.OpenIdServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KakaoOpenIdJwksErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response)throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        if(statusCode.isError()) {
            log.warn("Kakao JWKS 서버에서 {} 상태코드가 반환되었습니다.", statusCode.value());
            throw new OpenIdServerException(ExceptionType.OPEN_ID_PROVIDER_NOT_RESPONSE);
        }
        log.error("Kakao JWKS 서버에서 오류가 발생했습니다.");
        throw new OpenIdServerException(ExceptionType.OPEN_ID_SERVER_ERROR);
    }
}
