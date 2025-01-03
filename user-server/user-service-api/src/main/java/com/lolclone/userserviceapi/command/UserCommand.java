package com.lolclone.userserviceapi.command;

import java.util.UUID;

import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserCommand implements Command {
    private UUID userId;
}
