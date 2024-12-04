package com.lolclone.chat_server.dto.event;

import com.lolclone.chat_server.domain.UserStatus;
import lombok.Getter;

@Getter
public class UserStatusEvent {
    private final Long userId;
    private final UserStatus status;
    private final String gameInfo;
    
    private UserStatusEvent(Long userId, UserStatus status, String gameInfo) {
        this.userId = userId;
        this.status = status;
        this.gameInfo = gameInfo;
    }
    
    public static UserStatusEvent of(Long userId, UserStatus status) {
        return new UserStatusEvent(userId, status, null);
    }
    
    public static UserStatusEvent ofInGame(Long userId, String gameInfo) {
        return new UserStatusEvent(userId, UserStatus.IN_GAME, gameInfo);
    }
} 