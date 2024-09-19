package com.lolclone.authentication_management_server.config;

import com.lolclone.common_module.authenticationserver.AuthenticateContext;
import com.lolclone.common_module.authenticationserver.annotation.Authorization;
import com.lolclone.authentication_management_server.common.interceptor.AnnotationDelegateInterceptor;
import com.lolclone.authentication_management_server.common.interceptor.HttpMethodDelegateInterceptor;
import com.lolclone.authentication_management_server.domain.AuthenticationTokenExtractor;
import com.lolclone.common_module.authenticationserver.Role;
import com.lolclone.authentication_management_server.infrastructure.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class LoginConfig implements WebMvcConfigurer {
    private final AuthenticationTokenExtractor memberAuthenticationTokenExtractor;
    private final AuthenticationTokenExtractor compositeAuthenticationTokenExtractor;
    private final AuthenticateContext authenticateContext;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberAuthenticationArgumentResolver(authenticateContext));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(HttpMethodDelegateInterceptor.builder()
                .allowMethod(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH)
                .interceptor(memberFixedAuthorizationInterceptor())
                .build())
            .addPathPatterns("")
            .excludePathPatterns("/auth/auth2");
        registry.addInterceptor(AnnotationDelegateInterceptor.builder()
                .annotation(Authorization.class)
                .interceptor(annotationAuthorizationInterceptor())
                .build())
            .addPathPatterns("/api/**");
    }

    @Bean
    public CompositeHttpRequestTokenExtractor compositeHttpRequestTokenExtractor() {
        return new CompositeHttpRequestTokenExtractor(
                List.of(
                        new HeaderHttpRequestTokenExtractor()
                )
        );
    }

    @Bean
    public FixedAuthorizationInterceptor memberFixedAuthorizationInterceptor() {
        return new FixedAuthorizationInterceptor(
                compositeHttpRequestTokenExtractor(),
                memberAuthenticationTokenExtractor,
                authenticateContext,
                Role.MEMBER
        );
    }

    @Bean
    public AnnotationAuthorizationInterceptor annotationAuthorizationInterceptor() {
        return new AnnotationAuthorizationInterceptor(
                compositeHttpRequestTokenExtractor(),
                compositeAuthenticationTokenExtractor,
                authenticateContext
        );
    }

}
