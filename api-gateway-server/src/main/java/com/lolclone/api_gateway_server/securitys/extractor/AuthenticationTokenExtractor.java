package com.lolclone.api_gateway_server.securitys.extractor;


import com.lolclone.commonmodule.apigatewayserver.domain.Authentication;
import reactor.core.publisher.Mono;

public interface AuthenticationTokenExtractor {
    Mono<Authentication> extract(String token);
}
