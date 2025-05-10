package com.lolclone.authenticationmanagementinfra.service.application;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CustomJdbcOAuth2AuthorizedClientService extends JdbcOAuth2AuthorizedClientService {

    public CustomJdbcOAuth2AuthorizedClientService(
        JdbcOperations jdbcOperations,
        ClientRegistrationRepository clientRegistrationRepository
    ) {
        super(jdbcOperations, clientRegistrationRepository);
    }
}
