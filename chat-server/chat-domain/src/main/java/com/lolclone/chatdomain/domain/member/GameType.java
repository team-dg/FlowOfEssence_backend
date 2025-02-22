package com.lolclone.chatdomain.domain.member;

public enum GameType {
    SOLO_RANK("솔로랭크"),
    FLEX_RANK("자유랭크"),
    NORMAL("일반"),
    ARAM("무작위 총력전"),
    SPECIAL("특별 게임 모드");

    private final String displayName;

    GameType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
