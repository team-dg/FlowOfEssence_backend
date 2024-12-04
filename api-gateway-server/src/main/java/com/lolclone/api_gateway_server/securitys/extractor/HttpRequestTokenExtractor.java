package com.lolclone.api_gateway_server.securitys.extractor;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface HttpRequestTokenExtractor {
    Mono<String> extract(ServerWebExchange request);
}
