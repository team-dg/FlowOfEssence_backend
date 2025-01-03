package com.lolclone.userserviceapi.command;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UndoCreateUserCommand extends UserCommand {
    public UndoCreateUserCommand(UUID userId) {
        super(userId);
    }
}
