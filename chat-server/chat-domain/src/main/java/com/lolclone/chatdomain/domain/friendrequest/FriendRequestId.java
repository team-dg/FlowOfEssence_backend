package com.lolclone.chatdomain.domain.friendrequest;

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
public class FriendRequestId implements Serializable {
     @Column(name = "request_id")
    private UUID id;

    private FriendRequestId(UUID id) {
        this.id = id;
    }

    public static FriendRequestId newId() {
        return new FriendRequestId(UUID.randomUUID());
    }

    public static FriendRequestId of(UUID id) {
        return new FriendRequestId(id);
    }

    public UUID getValue() {
        return this.id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
