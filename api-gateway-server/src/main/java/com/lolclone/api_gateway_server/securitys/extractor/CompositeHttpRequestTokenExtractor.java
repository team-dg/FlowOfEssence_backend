package com.lolclone.api_gateway_server.securitys.extractor;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class CompositeHttpRequestTokenExtractor implements HttpRequestTokenExtractor {
    private final List<HttpRequestTokenExtractor> httpRequestTokenExtractors;

    @Override
    public Mono<String> extract(ServerWebExchange exchange) {
        return Flux.fromIterable(httpRequestTokenExtractors)
            .flatMap(extractor -> extractor.extract(exchange))
            .next();
    }
}
