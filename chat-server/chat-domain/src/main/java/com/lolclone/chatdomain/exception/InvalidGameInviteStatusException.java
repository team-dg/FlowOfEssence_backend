package com.lolclone.chatdomain.exception;

import com.lolclone.chatdomain.domain.gameinvite.GameInviteId;

public class InvalidGameInviteStatusException extends RuntimeException {
    private final GameInviteId gameInviteId;

    public InvalidGameInviteStatusException(String message, GameInviteId gameInviteId) {
        super(message);
        this.gameInviteId = gameInviteId;
    }

    public GameInviteId getGameInviteId() {
        return gameInviteId;
    }
}

