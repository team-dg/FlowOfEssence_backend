package com.lolclone.userinfra.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.lolclone.commonmodule.config.CommonConfiguration;
import com.lolclone.userinfra.saga.command.UserCommandHandler;
import com.lolclone.userinfra.service.domain.UserServiceImpl;

import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;

@Configuration
@Import({ 
    CommonConfiguration.class,
})
@EnableAutoConfiguration
public class UserCommandHandlersConfiguration {
    
    @Bean
    public UserCommandHandler userCommandHandlers(UserServiceImpl userService) {
        return new UserCommandHandler(userService);
    }

    @Bean
    public SagaCommandDispatcher userCommandDispatcher(UserCommandHandler userCommandHandler, SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("userServiceCommandDispatcher", userCommandHandler.commandHandlers());
    }
}
