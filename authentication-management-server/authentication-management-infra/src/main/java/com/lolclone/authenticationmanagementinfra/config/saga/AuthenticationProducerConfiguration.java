package com.lolclone.authenticationmanagementinfra.config.saga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lolclone.authenticationmanagementinfra.sagaorchestrator.producer.AuthenticationDomainEventPublisher;

import io.eventuate.tram.events.publisher.DomainEventPublisher;

@Configuration
public class AuthenticationProducerConfiguration {
    @Bean
    public AuthenticationDomainEventPublisher authenticationDomainEventPublisher(
        DomainEventPublisher eventPublisher
    ) {
        return new AuthenticationDomainEventPublisher(eventPublisher);
    }
}
