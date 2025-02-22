package com.lolclone.chatinfra.config.saga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.lolclone.chatinfra.saga.command.ChatCommandHandlers;
import com.lolclone.chatinfra.service.domain.MemberService;

import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;

@Configuration
@Import(TramEventSubscriberConfiguration.class)
public class ChatCommandHandlersConfiguration {
    @Bean
    public ChatCommandHandlers chatCommandHandlers(MemberService memberService) {
        return new ChatCommandHandlers(memberService);
    }

    @Bean
    public SagaCommandDispatcher chatCommandHandlersDispatcher(ChatCommandHandlers chatCommandHandlers, SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("chatServiceCommandDispatcher", chatCommandHandlers.commandHandlers());
    }
}
