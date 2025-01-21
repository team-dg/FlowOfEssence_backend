package com.lolclone.chatdomain.exception;

import com.lolclone.chatdomain.domain.gameinvite.GameInviteId;

public class UnauthorizedGameInviteException extends RuntimeException {
    private final GameInviteId gameInviteId;

    public UnauthorizedGameInviteException(String message, GameInviteId gameInviteId) {
        super(message);
        this.gameInviteId = gameInviteId;
    }

    public GameInviteId getGameInviteId() {
        return gameInviteId;
    }
}
