package com.lolclone.authenticationmanagementinfra.sagaorchestrator.sagaparticipants;

import com.lolclone.commonmodule.channel.ChannelNames;
import com.lolclone.userserviceapi.command.CreateUserCommand;
import com.lolclone.userserviceapi.command.UndoCreateUserCommand;

import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;

public class UserServiceProxy {
    public final CommandEndpoint<CreateUserCommand> createUser = CommandEndpointBuilder
          .forCommand(CreateUserCommand.class)
          .withChannel(ChannelNames.USER_SERVICE)
          .withReply(Success.class)
          .build();

    public final CommandEndpoint<UndoCreateUserCommand> undoCreateUser = CommandEndpointBuilder
          .forCommand(UndoCreateUserCommand.class)
          .withChannel(ChannelNames.USER_SERVICE)
          .withReply(Success.class)
          .build();
}
