package com.lolclone.authenticationmanagementinfra.sagaorchestrator.command;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementinfra.service.application.UserAuthService;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;
import com.lolclone.authenticationmanagementserviceapi.command.CreateSignUpUserCommand;
import com.lolclone.authenticationmanagementserviceapi.command.UndoCreateSignUpUserCommand;
import com.lolclone.commonmodule.channel.ChannelNames;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationCommandHandlers {
    private final UserAuthService userAuthService;
    private final MemberService memberService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
            .fromChannel(ChannelNames.AUTHENTICATION_SERVICE)
            .onMessage(CreateSignUpUserCommand.class, this::handleCreateSignUpUserCommand)
            .onMessage(UndoCreateSignUpUserCommand.class, this::handleUndoCreateSignUpUserCommand)
            .build();
    }

    public Message handleCreateSignUpUserCommand(CommandMessage<CreateSignUpUserCommand> cm) {
        UUID userId = cm.getCommand().getUserId();
        memberService.createSignUpUser(userId);
        return withSuccess();
    }

    @Transactional
    public Message handleUndoCreateSignUpUserCommand(CommandMessage<UndoCreateSignUpUserCommand> cm) {
        UUID userId = cm.getCommand().getUserId();
        UUID refreshTokenId = cm.getCommand().getRefreshTokenId();
        userAuthService.deleteAccount(userId);
        userAuthService.logout(userId, refreshTokenId);
        return withSuccess();
    }
}
