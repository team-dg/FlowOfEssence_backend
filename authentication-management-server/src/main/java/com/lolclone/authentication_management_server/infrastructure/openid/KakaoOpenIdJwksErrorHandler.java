package com.lolclone.authentication_management_server.infrastructure.openid;

import com.lolclone.common_module.commonexception.InternalServerException;
import com.lolclone.common_module.exception.domain.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class KakaoOpenIdJwksErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response)throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        if(statusCode.isError()) {
            log.warn("Kakao JWKS 서버에서 {} 상태코드가 반환되었습니다.", statusCode.value());
            throw new InternalServerException(ExceptionType.OPEN_ID_PROVIDER_NOT_RESPONSE);
        }
        log.error("Kakao JWKS 서버에서 오류가 발생했습니다.");
        throw new InternalServerException(ExceptionType.SERVER_ERROR);
    }
}
