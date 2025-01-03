package com.lolclone.userdomain.entity;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UndoCreateUserEvent implements UserDomainEvent {
    private UUID userId;

    public UndoCreateUserEvent(UUID userId) {
        this.userId = userId;
    }
}
