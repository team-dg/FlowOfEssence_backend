package com.lolclone.authenticationmanagementserviceapi.command;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UndoCreateSignUpUserCommand extends AuthenticationCommand {
    private UUID refreshTokenId;

    public UndoCreateSignUpUserCommand(UUID userId, UUID refreshTokenId) {
        super(userId);
        this.refreshTokenId = refreshTokenId;
    }
}
