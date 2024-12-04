package com.lolclone.api_gateway_server.securitys.interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

public class HttpMethodDelegateInterceptor implements WebFilter {
    private final Set<String> allowMethods;
    private final WebFilter filter;

    protected HttpMethodDelegateInterceptor(Set<String> allowMethods, WebFilter filter) {
        this.allowMethods = allowMethods;
        this.filter = filter;
    }

    public static HttpMethodDelegateInterceptorBuilder builder() {
        return new HttpMethodDelegateInterceptorBuilder();
    }

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        if (allowMethods.contains(exchange.getRequest().getMethod().name())) {
            return filter.filter(exchange, chain);
        }
        return chain.filter(exchange);
    }

    public static class HttpMethodDelegateInterceptorBuilder {

        private final Set<HttpMethod> allowMethod = new HashSet<>();
        private WebFilter filter;

        public HttpMethodDelegateInterceptorBuilder allowMethod(HttpMethod... httpMethods) {
            allowMethod.addAll(Arrays.asList(httpMethods));
            return this;
        }

        public HttpMethodDelegateInterceptorBuilder interceptor(WebFilter filter) {
            this.filter = filter;
            return this;
        }

        public HttpMethodDelegateInterceptor build() {
            Set<String> methods = allowMethod.stream()
                    .map(HttpMethod::name)
                    .collect(toUnmodifiableSet());
            return new HttpMethodDelegateInterceptor(methods, filter);
        }
    }
}
