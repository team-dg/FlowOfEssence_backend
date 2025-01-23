package com.lolclone.chatdomain.exception;

import java.util.UUID;

public class InvalidGameInviteStatusException extends RuntimeException {
    private final UUID gameInviteId;

    public InvalidGameInviteStatusException(String message, UUID gameInviteId) {
        super(message);
        this.gameInviteId = gameInviteId;
    }

    public UUID getGameInviteId() {
        return gameInviteId;
    }
}

