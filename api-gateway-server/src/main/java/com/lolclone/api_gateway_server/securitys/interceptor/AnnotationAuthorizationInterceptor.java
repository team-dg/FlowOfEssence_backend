package com.lolclone.api_gateway_server.securitys.interceptor;

import com.lolclone.api_gateway_server.common.exception.commonexception.UnauthorizedException;
import com.lolclone.api_gateway_server.common.exception.commonexception.UnexpectedException;
import com.lolclone.api_gateway_server.domain.ReactiveAuthenticateContext;
import com.lolclone.api_gateway_server.common.exception.commonexception.ForbiddenException;
import com.lolclone.api_gateway_server.common.exception.domain.ExceptionType;
import com.lolclone.commonmodule.apigatewayserver.annotation.Authorization;
import io.jsonwebtoken.lang.Assert;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.function.server.support.RouterFunctionMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.lolclone.api_gateway_server.securitys.extractor.HttpRequestTokenExtractor;
import com.lolclone.api_gateway_server.securitys.extractor.AuthenticationTokenExtractor;

import reactor.core.publisher.Mono;

@Component
public class AnnotationAuthorizationInterceptor implements WebFilter {
    private final HttpRequestTokenExtractor httpRequestTokenExtractor;
    private final AuthenticationTokenExtractor authenticationTokenExtractor;
    private final ReactiveAuthenticateContext authenticateContext;
    private final RouterFunctionMapping routerFunctionMapping;

    public AnnotationAuthorizationInterceptor(
        HttpRequestTokenExtractor httpRequestTokenExtractor,
        AuthenticationTokenExtractor authenticationTokenExtractor,
        ReactiveAuthenticateContext authenticateContext,
        RouterFunctionMapping routerFunctionMapping
    ) {
        Assert.notNull(httpRequestTokenExtractor, "The httpRequestTokenExtractor must not be null");
        Assert.notNull(authenticationTokenExtractor, "The authenticationTokenExtractor must not be null");
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        Assert.notNull(routerFunctionMapping, "The routerFunctionMapping must not be null");
        this.httpRequestTokenExtractor = httpRequestTokenExtractor;
        this.authenticationTokenExtractor = authenticationTokenExtractor;
        this.authenticateContext = authenticateContext;
        this.routerFunctionMapping = routerFunctionMapping;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return routerFunctionMapping.getHandler(exchange)
            .cast(HandlerMethod.class)
            .flatMap(handlerMethod -> {
                Authorization authorization = handlerMethod.getMethodAnnotation(Authorization.class);
                if (authorization == null) {
                    return Mono.error(new UnexpectedException("HandlerMethod에 Authorization 어노테이션이 없습니다."));
                }
                
                return httpRequestTokenExtractor.extract(exchange)
                    .switchIfEmpty(Mono.error(new UnauthorizedException(ExceptionType.NEED_AUTH_TOKEN)))
                    .flatMap(authenticationTokenExtractor::extract)
                    .flatMap(authentication -> {
                        if (authentication.getRole() != authorization.role()) {
                            return Mono.error(new ForbiddenException(ExceptionType.NOT_ENOUGH_PERMISSION));
                        }
                        return authenticateContext.setAuthentication(exchange, authentication)
                            .then(chain.filter(exchange));
                    });
            })
            .switchIfEmpty(chain.filter(exchange));
    }
}
