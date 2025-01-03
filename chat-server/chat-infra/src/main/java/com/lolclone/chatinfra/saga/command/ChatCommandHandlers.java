package com.lolclone.chatinfra.saga.command;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.exception.UnsupportedStateTransitionException;
import com.lolclone.chatinfra.service.domain.MemberService;
import com.lolclone.chatserviceapi.command.CreateUserCommand;
import com.lolclone.chatserviceapi.command.UndoCreateUserCommand;
import com.lolclone.commonmodule.channel.ChannelNames;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatCommandHandlers {
    private final MemberService memberService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(ChannelNames.USER_SERVICE)
                .onMessage(CreateUserCommand.class, this::CreateUserCommand)
                .onMessage(UndoCreateUserCommand.class, this::UndoCreateUserCommand)
                .build();
    }

    @Transactional
    public Message CreateUserCommand(CommandMessage<CreateUserCommand> cm) {
        UUID userId = cm.getCommand().getUserId();
        String nickname = cm.getCommand().getNickname();
        try {
            memberService.createMember(userId, nickname);
            return withSuccess();
        } catch (UnsupportedStateTransitionException e) {
            return withFailure();
        }
    }

    @Transactional
    public Message UndoCreateUserCommand(CommandMessage<UndoCreateUserCommand> cm) {
        UUID userId = cm.getCommand().getUserId();
        memberService.undoCreateMember(userId);
        return withSuccess();
    }
}
