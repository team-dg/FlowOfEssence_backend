package com.lolclone.chatserviceapi.command;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateUserCommand extends ChatCommand {
    private String nickname;

    public CreateUserCommand(UUID userId, String nickname) {
        super(userId);
        this.nickname = nickname;
    }
}
