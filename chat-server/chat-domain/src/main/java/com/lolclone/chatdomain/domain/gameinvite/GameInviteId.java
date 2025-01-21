package com.lolclone.chatdomain.domain.gameinvite;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class GameInviteId implements Serializable {
    @Column(name = "invite_id")
    private UUID id;

    private GameInviteId(UUID id) {
        this.id = id;
    }

    public static GameInviteId newId() {
        return new GameInviteId(UUID.randomUUID());
    }

    public static GameInviteId of(UUID id) {
        return new GameInviteId(id);
    }

    public UUID getValue() {
        return this.id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
