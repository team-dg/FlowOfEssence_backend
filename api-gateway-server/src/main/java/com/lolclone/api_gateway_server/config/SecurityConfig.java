package com.lolclone.api_gateway_server.config;

import com.lolclone.api_gateway_server.domain.ReactiveAuthenticateContext;
import com.lolclone.api_gateway_server.securitys.extractor.AuthenticationTokenExtractor;
import com.lolclone.api_gateway_server.securitys.extractor.HttpRequestTokenExtractor;
import com.lolclone.api_gateway_server.securitys.interceptor.FixedAuthorizationInterceptor;
import com.lolclone.commonmodule.apigatewayserver.domain.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class SecurityConfig {

    @Bean
    public FixedAuthorizationInterceptor fixedAuthorizationInterceptor(
            HttpRequestTokenExtractor httpRequestTokenExtractor,
            AuthenticationTokenExtractor authenticationTokenExtractor,
            ReactiveAuthenticateContext authenticateContext
    ) {
        return new FixedAuthorizationInterceptor(
                httpRequestTokenExtractor,
                authenticationTokenExtractor,
                authenticateContext,
                Role.MEMBER
        );
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
