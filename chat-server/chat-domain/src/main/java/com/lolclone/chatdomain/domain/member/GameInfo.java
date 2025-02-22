package com.lolclone.chatdomain.domain.member;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameInfo {
    @Column(name = "game_session_id")
    private UUID gameSessionId;

    @Column(name = "post_game_chat_room_id")
    private UUID postGameChatRoomId;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type")
    private GameType gameType;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_mode")
    private GameMode gameMode;

    @Column(name = "game_start_time")
    private LocalDateTime gameStartTime;

    @Builder
    private GameInfo(GameType gameType, GameMode gameMode) {
        this.gameType = gameType;
        this.gameMode = gameMode;
        this.gameStartTime = LocalDateTime.now();
    }

    public String getDisplayText() {
        if (gameType == null || gameMode == null) {
            return null;
        }
        return String.format("%s %s 게임 중", gameMode.getDisplayName(), gameType.getDisplayName());
    }

    public static GameInfo createGameInfo(GameType gameType, GameMode gameMode) {
        return GameInfo.builder()
            .gameType(gameType)
            .gameMode(gameMode)
            .build();
    }

    public static GameInfo empty() {
        return new GameInfo();
    }
}
