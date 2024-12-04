package com.lolclone.api_gateway_server.securitys.extractor;

import com.lolclone.commonmodule.apigatewayserver.domain.Authentication;
import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

public interface AuthenticationClaimsExtractor {
    Mono<Authentication> extract(Claims claims);
}
