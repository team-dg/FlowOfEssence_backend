package com.lolclone.authentication_management_server.config;

import com.lolclone.authentication_management_server.application.OAuth2Client;
import com.lolclone.authentication_management_server.application.OAuth2Clients;
import com.lolclone.authentication_management_server.domain.OpenIdClient;
import com.lolclone.authentication_management_server.domain.OpenIdClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
