package com.lolclone.authenticationmanagementinfra.sagaorchestrator.sagaparticipants;

import com.lolclone.authenticationmanagementserviceapi.command.CreateSignUpUserCommand;
import com.lolclone.authenticationmanagementserviceapi.command.UndoCreateSignUpUserCommand;
import com.lolclone.commonmodule.channel.ChannelNames;

import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;

public class AuthenticationServiceProxy {
    public final CommandEndpoint<CreateSignUpUserCommand> createUser = CommandEndpointBuilder
          .forCommand(CreateSignUpUserCommand.class)
          .withChannel(ChannelNames.AUTHENTICATION_SERVICE)
          .withReply(Success.class)
          .build();

    public final CommandEndpoint<UndoCreateSignUpUserCommand> undoCreateUser = CommandEndpointBuilder
            .forCommand(UndoCreateSignUpUserCommand.class)
            .withChannel(ChannelNames.AUTHENTICATION_SERVICE)
            .withReply(Success.class)
            .build();
}
