package com.lolclone.chatinfra.config.saga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lolclone.chatinfra.saga.producer.ChatDomainEventPublisher;

import io.eventuate.tram.events.publisher.DomainEventPublisher;

@Configuration
public class ChatProducerConfiguration {
    @Bean
    public ChatDomainEventPublisher chatDomainEventPublisher(
        DomainEventPublisher eventPublisher
    ) {
        return new ChatDomainEventPublisher(eventPublisher);
    }
}
