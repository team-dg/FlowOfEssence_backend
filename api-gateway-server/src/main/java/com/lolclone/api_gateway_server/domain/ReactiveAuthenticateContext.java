package com.lolclone.api_gateway_server.domain;

import com.lolclone.commonmodule.apigatewayserver.domain.AnonymousAuthentication;
import com.lolclone.commonmodule.apigatewayserver.domain.Authentication;
import com.lolclone.commonmodule.apigatewayserver.domain.Role;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ReactiveAuthenticateContext {
    private static final String AUTHENTICATION_KEY = "AUTHENTICATION_CONTEXT";

    public Mono<Void> setAuthentication(ServerWebExchange exchange, Authentication authentication) {
        exchange.getAttributes().put(AUTHENTICATION_KEY, authentication);
        return Mono.empty();
    }

    public Mono<Authentication> getAuthentication(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getAttribute(AUTHENTICATION_KEY))
                .cast(Authentication.class)
                .defaultIfEmpty(AnonymousAuthentication.getInstance());
    }

    public Mono<UUID> getId(ServerWebExchange exchange) {
        return getAuthentication(exchange)
                .map(Authentication::getId);
    }

    public Mono<Role> getRole(ServerWebExchange exchange){
        return getAuthentication(exchange)
                .map(Authentication::getRole);
    }
}
