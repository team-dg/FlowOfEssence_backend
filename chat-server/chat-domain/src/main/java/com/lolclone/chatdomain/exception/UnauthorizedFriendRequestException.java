package com.lolclone.chatdomain.exception;

import com.lolclone.chatdomain.domain.friendrequest.FriendRequestId;

public class UnauthorizedFriendRequestException extends RuntimeException {
    private final FriendRequestId friendRequestId;

    public UnauthorizedFriendRequestException(String message, FriendRequestId friendRequestId) {
        super(message);
        this.friendRequestId = friendRequestId;
    }

    public FriendRequestId getFriendRequestId() {
        return friendRequestId;
    }
}
