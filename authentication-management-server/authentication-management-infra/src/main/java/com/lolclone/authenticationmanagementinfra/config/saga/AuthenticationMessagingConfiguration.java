package com.lolclone.authenticationmanagementinfra.config.saga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lolclone.authenticationmanagementinfra.sagaorchestrator.consumer.AuthenticationDomainEventConsumer;
import com.lolclone.authenticationmanagementinfra.service.application.UserAuthService;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;

@Configuration
public class AuthenticationMessagingConfiguration {
    @Bean
    public AuthenticationDomainEventConsumer authenticationDomainEventConsumer(UserAuthService userAuthService) {
        return new AuthenticationDomainEventConsumer(userAuthService);
    }
    
    @Bean
    public DomainEventDispatcher domainEventDispatcher(AuthenticationDomainEventConsumer authenticationDomainEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make("authenticationServiceEvents", authenticationDomainEventConsumer.domainEventHandlers());
    }
}
