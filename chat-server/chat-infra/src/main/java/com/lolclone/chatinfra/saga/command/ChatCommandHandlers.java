package com.lolclone.chatinfra.saga.command;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import java.util.UUID;

import com.lolclone.chatdomain.exception.UnsupportedStateTransitionException;
import com.lolclone.chatinfra.service.domain.MemberService;
import com.lolclone.chatserviceapi.command.CreateUserChatCommand;
import com.lolclone.chatserviceapi.command.UndoCreateUserChatCommand;
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
                .fromChannel(ChannelNames.CHAT_SERVICE)
                .onMessage(CreateUserChatCommand.class, this::CreateUserChatCommand)
                .onMessage(UndoCreateUserChatCommand.class, this::UndoCreateUserChatCommand)
                .build();
    }

    public Message CreateUserChatCommand(CommandMessage<CreateUserChatCommand> cm) {
        UUID userId = cm.getCommand().getUserId();
        String nickname = cm.getCommand().getNickname();
        try {
            memberService.createMember(userId, nickname);
            return withSuccess();
        } catch (UnsupportedStateTransitionException e) {
            return withFailure();
        }
    }

    public Message UndoCreateUserChatCommand(CommandMessage<UndoCreateUserChatCommand> cm) {
        UUID userId = cm.getCommand().getUserId();
        memberService.undoCreateMember(userId);
        return withSuccess();
    }
}
