package com.lolclone.authenticationmanagementserviceapi.command;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateSignUpUserCommand extends AuthenticationCommand {
    private String nickname;

    public CreateSignUpUserCommand(UUID userId, String nickname) {
        super(userId);
        this.nickname = nickname;
    }
}
