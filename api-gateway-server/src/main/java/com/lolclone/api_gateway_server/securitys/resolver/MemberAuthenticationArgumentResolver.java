package com.lolclone.api_gateway_server.securitys.resolver;

import com.lolclone.api_gateway_server.common.exception.commonexception.UnexpectedException;
import com.lolclone.api_gateway_server.domain.ReactiveAuthenticateContext;
import com.lolclone.commonmodule.apigatewayserver.domain.MemberAuthentication;
import io.jsonwebtoken.lang.Assert;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;

import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class MemberAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    private final ReactiveAuthenticateContext authenticateContext;

    public MemberAuthenticationArgumentResolver(ReactiveAuthenticateContext authenticateContext) {
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        this.authenticateContext = authenticateContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberAuthentication.class);
    }

    @NotNull
    @Override
    public Mono<Object> resolveArgument(@NotNull MethodParameter parameter, @NotNull BindingContext bindingContext, @NotNull ServerWebExchange exchange) {
        return authenticateContext.getAuthentication(exchange)
                .filter(auth -> auth instanceof MemberAuthentication)
                .cast(MemberAuthentication.class)
                .cast(Object.class)
                .switchIfEmpty(Mono.error(new UnexpectedException("인가된 권한이 인자의 권한과 맞지 않습니다.")));
    }
}
