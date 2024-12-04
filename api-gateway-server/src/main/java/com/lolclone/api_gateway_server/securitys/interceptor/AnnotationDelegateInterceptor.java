package com.lolclone.api_gateway_server.securitys.interceptor;

import io.jsonwebtoken.lang.Assert;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.function.server.support.RouterFunctionMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;

public class AnnotationDelegateInterceptor implements WebFilter {
    private final Class<? extends Annotation> annotation;
    private final WebFilter filter;
    private final RouterFunctionMapping routerFunctionMapping;

    protected AnnotationDelegateInterceptor(
        Class<? extends Annotation> annotation,
        WebFilter filter,
        RouterFunctionMapping routerFunctionMapping
    ) {
        Assert.notNull(annotation, "annotation은 null이 될 수 없습니다.");
        Assert.notNull(filter, "filter는 null이 될 수 없습니다.");
        Assert.notNull(routerFunctionMapping, "routerFunctionMapping은 null이 될 수 없습니다.");
        this.annotation = annotation;
        this.filter = filter;
        this.routerFunctionMapping = routerFunctionMapping;
    }

    public static AnnotationsDelegateInterceptorBuilder builder() {
        return new AnnotationsDelegateInterceptorBuilder();
    }

    @NotNull
    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, WebFilterChain chain) {
        return routerFunctionMapping.getHandler(exchange)
                .cast(HandlerMethod.class)
                .map(handlerMethod -> handlerMethod.hasMethodAnnotation(annotation))
                .flatMap(hasAnnotation -> hasAnnotation ? filter.filter(exchange, chain) : chain.filter(exchange))
                .switchIfEmpty(chain.filter(exchange));
    }

    public static class AnnotationsDelegateInterceptorBuilder {
        private Class<? extends Annotation> annotation;
        private WebFilter filter;
        private RouterFunctionMapping routerFunctionMapping;

        public AnnotationsDelegateInterceptorBuilder annotation(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
            return this;
        }

        public AnnotationsDelegateInterceptorBuilder filter(WebFilter filter) {
            this.filter = filter;
            return this;
        }

        public AnnotationsDelegateInterceptorBuilder routerFunctionMapping(RouterFunctionMapping routerFunctionMapping) {
            this.routerFunctionMapping = routerFunctionMapping;
            return this;
        }

        public AnnotationDelegateInterceptor build() {
            return new AnnotationDelegateInterceptor(annotation, filter, routerFunctionMapping);
        }
    }
}
