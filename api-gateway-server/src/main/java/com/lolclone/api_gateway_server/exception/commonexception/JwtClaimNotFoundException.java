package com.lolclone.api_gateway_server.exception.commonexception;

import com.lolclone.api_gateway_server.exception.domain.ExceptionType;

public class JwtClaimNotFoundException extends ApiGatewayException {
    public JwtClaimNotFoundException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
