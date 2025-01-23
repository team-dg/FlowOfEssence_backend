package com.lolclone.authenticationmanagementinfra.sagaorchestrator.saga;

import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.lolclone.authenticationmanagementserviceapi.command.CreateSignUpUserCommand;
import com.lolclone.authenticationmanagementserviceapi.command.UndoCreateSignUpUserCommand;
import com.lolclone.chatserviceapi.command.CreateUserChatCommand;
import com.lolclone.chatserviceapi.command.UndoCreateUserChatCommand;
import com.lolclone.userserviceapi.command.CreateUserCommand;
import com.lolclone.userserviceapi.command.UndoCreateUserCommand;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SignUpSagaState {
    private UUID userId;
    private String nickname;

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public CreateUserCommand makeCreateUserCommand() {
        return new CreateUserCommand(getUserId(), getNickname());
    }

    public UndoCreateUserCommand makeUndoCreateUserCommand() {
        return new UndoCreateUserCommand(getUserId());
    }

    public CreateUserChatCommand makeCreateUserChatCommand() {
        return new CreateUserChatCommand(getUserId(), getNickname());
    }

    public UndoCreateUserChatCommand makeUndoCreateUserChatCommand() {
        return new UndoCreateUserChatCommand(getUserId());
    }

    public CreateSignUpUserCommand makeCreateSignUpUserCommand() {
        return new CreateSignUpUserCommand(getUserId(), getNickname());
    }

    public UndoCreateSignUpUserCommand makeUndoCreateSignUpUserCommand() {
        return new UndoCreateSignUpUserCommand(getUserId());
    }
}
