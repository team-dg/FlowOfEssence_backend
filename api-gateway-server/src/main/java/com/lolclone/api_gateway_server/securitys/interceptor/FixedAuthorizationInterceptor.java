package com.lolclone.api_gateway_server.securitys.interceptor;

import com.lolclone.api_gateway_server.common.exception.commonexception.ForbiddenException;
import com.lolclone.api_gateway_server.common.exception.commonexception.UnauthorizedException;
import com.lolclone.api_gateway_server.common.exception.domain.ExceptionType;
import com.lolclone.api_gateway_server.domain.ReactiveAuthenticateContext;
import com.lolclone.api_gateway_server.securitys.extractor.AuthenticationTokenExtractor;
import com.lolclone.api_gateway_server.securitys.extractor.HttpRequestTokenExtractor;
import com.lolclone.commonmodule.apigatewayserver.domain.Role;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
public class FixedAuthorizationInterceptor implements WebFilter {
    private final HttpRequestTokenExtractor httpRequestTokenExtractor;
    private final AuthenticationTokenExtractor authenticationTokenExtractor;
    private final ReactiveAuthenticateContext authenticateContext;
    private final Role role;

    public FixedAuthorizationInterceptor(
            HttpRequestTokenExtractor httpRequestTokenExtractor,
            AuthenticationTokenExtractor authenticationTokenExtractor,
            ReactiveAuthenticateContext authenticateContext,
            Role role
    ) {
        Assert.notNull(httpRequestTokenExtractor, "The httpRequestTokenExtractor must not be null");
        Assert.notNull(authenticationTokenExtractor, "The authenticationTokenExtractor must not be null");
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        Assert.notNull(role, "The role must not be null");
        this.httpRequestTokenExtractor = httpRequestTokenExtractor;
        this.authenticationTokenExtractor = authenticationTokenExtractor;
        this.authenticateContext = authenticateContext;
        this.role = role;
    }

    @NotNull
    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        return httpRequestTokenExtractor.extract(exchange)
                .switchIfEmpty(Mono.error(new UnauthorizedException(ExceptionType.NEED_AUTH_TOKEN)))
                .flatMap(authenticationTokenExtractor::extract)
                .flatMap(authentication -> {
                    if (authentication.getRole() != role) {
                        return Mono.error(new ForbiddenException(ExceptionType.NOT_ENOUGH_PERMISSION));
                    }
                    return authenticateContext.setAuthentication(exchange, authentication)
                            .then(chain.filter(exchange));
                });
    }
}
