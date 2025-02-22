package com.lolclone.chatdomain.domain.member;

public enum GameMode {
    SUMMONERS_RIFT("소환사의 협곡"),
    HOWLING_ABYSS("칼바람 나락"),
    SPECIAL_MODE("특별 모드");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}