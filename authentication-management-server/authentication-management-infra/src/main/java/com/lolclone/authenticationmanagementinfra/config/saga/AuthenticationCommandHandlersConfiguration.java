package com.lolclone.authenticationmanagementinfra.config.saga;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.lolclone.authenticationmanagementinfra.sagaorchestrator.command.AuthenticationCommandHandlers;
import com.lolclone.authenticationmanagementinfra.service.application.UserAuthService;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;

import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;

@Configuration
@Import(TramEventSubscriberConfiguration.class)
public class AuthenticationCommandHandlersConfiguration {
    @Bean
    public AuthenticationCommandHandlers authenticationCommandHandlers(UserAuthService userAuthService, MemberService memberService, ApplicationEventPublisher eventPublisher) {
        return new AuthenticationCommandHandlers(userAuthService, memberService, eventPublisher);
    }

    @Bean
    public SagaCommandDispatcher authenticationCommandHandlersDispatcher(AuthenticationCommandHandlers authenticationCommandHandlers, SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("authenticationServiceCommandDispatcher", authenticationCommandHandlers.commandHandlers());
    }
}
