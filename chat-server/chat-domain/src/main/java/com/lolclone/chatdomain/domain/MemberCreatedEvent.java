package com.lolclone.chatdomain.domain;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreatedEvent implements MemberDomainEvent {
    private final UUID userId;

    public MemberCreatedEvent(UUID userId) {
        this.userId = userId;
    }
}
