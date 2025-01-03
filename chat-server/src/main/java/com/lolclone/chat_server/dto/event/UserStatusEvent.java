package com.lolclone.chat_server.dto.event;

import java.util.UUID;

import com.lolclone.chat_server.domain.UserStatus;
import lombok.Getter;

@Getter
public class UserStatusEvent {
    private final UUID userId;
    private final UserStatus status;
    private final String gameInfo;
    
    private UserStatusEvent(UUID userId, UserStatus status, String gameInfo) {
        this.userId = userId;
        this.status = status;
        this.gameInfo = gameInfo;
    }
    
    public static UserStatusEvent of(UUID userId, UserStatus status) {
        return new UserStatusEvent(userId, status, null);
    }
    
    public static UserStatusEvent ofInGame(UUID userId, String gameInfo) {
        return new UserStatusEvent(userId, UserStatus.IN_GAME, gameInfo);
    }
} 