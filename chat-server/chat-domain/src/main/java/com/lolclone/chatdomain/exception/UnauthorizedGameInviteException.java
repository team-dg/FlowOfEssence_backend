package com.lolclone.chatdomain.exception;

import java.util.UUID;

public class UnauthorizedGameInviteException extends RuntimeException {
    private final UUID gameInviteId;

    public UnauthorizedGameInviteException(String message, UUID gameInviteId) {
        super(message);
        this.gameInviteId = gameInviteId;
    }

    public UUID getGameInviteId() {
        return gameInviteId;
    }
}
