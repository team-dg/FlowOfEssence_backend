package com.lolclone.userinfra.saga.command;

import com.lolclone.authenticationmanagementdomain.exception.UnsupportedStateTransitionException;
import com.lolclone.commonmodule.channel.ChannelNames;
import com.lolclone.userinfra.service.domain.UserServiceImpl;
import com.lolclone.userserviceapi.command.CreateUserCommand;
import com.lolclone.userserviceapi.command.UndoCreateUserCommand;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class UserCommandHandler {
    private final UserServiceImpl userService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(ChannelNames.USER_SERVICE)
                .onMessage(CreateUserCommand.class, this::CreateUserCommand)
                .onMessage(UndoCreateUserCommand.class, this::UndoCreateUserCommand)
                .build();
    }

    public Message CreateUserCommand(CommandMessage<CreateUserCommand> cm) {
        UUID userId = cm.getCommand().getUserId();
        String nickname = cm.getCommand().getNickname();
        try {
            userService.createUser(userId, nickname);
            return withSuccess();
        } catch(UnsupportedStateTransitionException e) {
            return withFailure();
        }
    }

    public Message UndoCreateUserCommand(CommandMessage<UndoCreateUserCommand> cm) {
        UUID userId = cm.getCommand().getUserId();
        userService.undoCreateUser(userId);
        return withSuccess();
    }
}
