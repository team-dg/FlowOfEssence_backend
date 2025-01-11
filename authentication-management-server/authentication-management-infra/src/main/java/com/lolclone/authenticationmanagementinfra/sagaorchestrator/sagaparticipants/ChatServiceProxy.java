package com.lolclone.authenticationmanagementinfra.sagaorchestrator.sagaparticipants;

import com.lolclone.commonmodule.channel.ChannelNames;
import com.lolclone.chatserviceapi.command.CreateUserChatCommand;
import com.lolclone.chatserviceapi.command.UndoCreateUserChatCommand;

import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;

public class ChatServiceProxy {
    public final CommandEndpoint<CreateUserChatCommand> createUserChat = CommandEndpointBuilder
          .forCommand(CreateUserChatCommand.class)
          .withChannel(ChannelNames.CHAT_SERVICE)
          .withReply(Success.class)
          .build();

    public final CommandEndpoint<UndoCreateUserChatCommand> undoCreateUserChat = CommandEndpointBuilder
          .forCommand(UndoCreateUserChatCommand.class)
          .withChannel(ChannelNames.CHAT_SERVICE)
          .withReply(Success.class)
          .build();
}
