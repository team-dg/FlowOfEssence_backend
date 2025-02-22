package com.lolclone.chatdomain.exception;

import java.util.UUID;

public class InvalidFriendRequestStatusException extends RuntimeException {
    private final UUID friendRequestId;

    public InvalidFriendRequestStatusException(String message, UUID friendRequestId) {
        super(message);
        this.friendRequestId = friendRequestId;
    }

    public UUID getFriendRequestId() {
        return friendRequestId;
    }
}

