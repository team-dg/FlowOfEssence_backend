package com.lolclone.authenticationmanagementinfra.config.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lolclone.authenticationmanagementdomain.domain.ServletAuthenticateContext;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Client;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Clients;
import com.lolclone.authenticationmanagementdomain.domain.openid.OpenIdClient;
import com.lolclone.authenticationmanagementdomain.domain.openid.OpenIdClients;
import com.lolclone.commonmodule.apigatewayserver.domain.AuthenticateContext;

@Configuration
public class AuthConfig {
    @Bean
    public OAuth2Clients oAuth2Clients(List<OAuth2Client> oAuth2Clients) {
        return OAuth2Clients.builder()
                .addAll(oAuth2Clients)
                .build();
    }

    @Bean
    public OpenIdClients openIdClients(List<OpenIdClient> openIdClients) {
        return OpenIdClients.builder()
                .addAll(openIdClients)
                .build();
    }

    @Bean
    public AuthenticateContext authenticateContext() {
        return new ServletAuthenticateContext();
    }
}
