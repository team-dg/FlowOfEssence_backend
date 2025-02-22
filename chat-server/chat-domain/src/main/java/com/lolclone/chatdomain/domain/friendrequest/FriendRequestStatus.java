package com.lolclone.chatdomain.domain.friendrequest;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequestStatus {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private FriendRequestStatus(Status status) {
        this.status = status;
    }

    public static FriendRequestStatus pending() {
        return new FriendRequestStatus(Status.PENDING);
    }

    public FriendRequestStatus accept() {
        return new FriendRequestStatus(Status.ACCEPTED);
    }

    public FriendRequestStatus reject() {
        return new FriendRequestStatus(Status.REJECTED);
    }

    public FriendRequestStatus cancel() {
        return new FriendRequestStatus(Status.CANCELED);
    }

    public boolean isPending() {
        return this.status == Status.PENDING;
    }

    public boolean isAccepted() {
        return this.status == Status.ACCEPTED;
    }

    public boolean isRejected() {
        return this.status == Status.REJECTED;
    }

    public boolean isCanceled() {
        return this.status == Status.CANCELED;
    }

    public enum Status {
        PENDING, ACCEPTED, REJECTED, CANCELED
    }
}
