package com.lolclone.userinfra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lolclone.userinfra.saga.publisher.UserDomainEventPublisher;

import io.eventuate.tram.events.publisher.DomainEventPublisher;

@Configuration
public class UserConfiguration {
    @Bean
    public UserDomainEventPublisher userDomainAggregateEventPublisher(DomainEventPublisher eventPublisher) {
        return new UserDomainEventPublisher(eventPublisher);
    }
}
