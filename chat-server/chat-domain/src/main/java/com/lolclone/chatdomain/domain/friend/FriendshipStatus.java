package com.lolclone.chatdomain.domain.friend;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendshipStatus {
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    private FriendshipStatus(Status status) {
        this.status = status;
    }

    public static FriendshipStatus of(Status status) {
        return new FriendshipStatus(status);
    }

    public static FriendshipStatus active() {
        return new FriendshipStatus(Status.ACTIVE);
    }

    public FriendshipStatus block() {
        return new FriendshipStatus(Status.BLOCKED);
    }

    public FriendshipStatus unblock() {
        return new FriendshipStatus(Status.ACTIVE);
    }

    public FriendshipStatus unfriend() {
        return new FriendshipStatus(Status.UNFRIENDED);
    }

    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

    public boolean isBlocked() {
        return this.status == Status.BLOCKED;
    }

    public enum Status {
        PENDING,
        ACTIVE,
        BLOCKED,
        UNFRIENDED
    }
}
