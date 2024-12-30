package com.lolclone.authenticationmanagementinfra.config.saga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lolclone.authenticationmanagementinfra.sagaorchestrator.handler.AuthenticationCommandHandlers;
import com.lolclone.authenticationmanagementinfra.service.application.UserAuthService;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;

import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;

@Configuration
public class AuthenticationCommandHandlersConfiguration {
    @Bean
    public AuthenticationCommandHandlers authenticationCommandHandlers(UserAuthService userAuthService, MemberService memberService) {
        return new AuthenticationCommandHandlers(userAuthService, memberService);
    }

    @Bean
    public SagaCommandDispatcher authenticationCommandHandlersDispatcher(AuthenticationCommandHandlers authenticationCommandHandlers, SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("authenticationServiceCommandDispatcher", authenticationCommandHandlers.commandHandlers());
    }
}
