package com.lolclone.api_gateway_server.securitys.extractor;

import com.lolclone.api_gateway_server.common.exception.commonexception.UnauthorizedException;
import com.lolclone.api_gateway_server.common.exception.domain.ExceptionType;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component("headerHttpRequestTokenExtractor")
public class HeaderHttpRequestTokenExtractor implements HttpRequestTokenExtractor {
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<String> extract(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return Mono.empty();
        }
        return Mono.just(extractToken(header));
    }

    private String extractToken(String header) {
        validateHeader(header);
        return header.substring(BEARER_TOKEN_PREFIX.length()).trim();
    }

    private void validateHeader(String header) {
        if (!header.toLowerCase().startsWith(BEARER_TOKEN_PREFIX.toLowerCase())) {
            throw new UnauthorizedException(ExceptionType.NOT_BEARER_TOKEN_TYPE);
        }
    }
}
