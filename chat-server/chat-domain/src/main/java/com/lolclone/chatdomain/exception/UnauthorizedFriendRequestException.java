package com.lolclone.chatdomain.exception;

import java.util.UUID;

public class UnauthorizedFriendRequestException extends RuntimeException {
    private final UUID friendRequestId;

    public UnauthorizedFriendRequestException(String message, UUID friendRequestId) {
        super(message);
        this.friendRequestId = friendRequestId;
    }

    public UUID getFriendRequestId() {
        return friendRequestId;
    }
}
