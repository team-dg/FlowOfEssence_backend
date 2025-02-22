package com.lolclone.chatdomain.domain;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UndoCreateMemberEvent implements MemberDomainEvent {
    private UUID userId;

    public UndoCreateMemberEvent(UUID userId) {
        this.userId = userId;
    }
}
