package com.lolclone.chatserviceapi.command;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UndoCreateUserCommand extends ChatCommand {
    public UndoCreateUserCommand(UUID userId) {
        super(userId);
    }
}
