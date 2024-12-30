package com.lolclone.userdomain.entity;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreatedEvent implements UserDomainEvent {
    private final UUID userId;

    public UserCreatedEvent(UUID userId) {
        this.userId = userId;
    }
}
