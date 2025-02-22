package com.lolclone.chatserviceapi.command;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UndoCreateUserChatCommand extends ChatCommand {
    public UndoCreateUserChatCommand(UUID userId) {
        super(userId);
    }
}
