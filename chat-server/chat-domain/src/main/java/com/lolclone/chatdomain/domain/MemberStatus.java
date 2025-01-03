package com.lolclone.chatdomain.domain;

import lombok.Getter;

@Getter
public enum MemberStatus {
    // 기본상태
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    AWAY("자리 비움"),

    // 게임 매칭 관련 상태
    QUEUING_SOLO("랭크 게임 찾는 중"),
    QUEUING_FLEX("자유 랭크 찾는 중"),
    QUEUING_NORMAL("일반 게임 찾는 중"),
    QUEUING_ARAM("무작위 총력전 찾는 중"),

    // 게임 준비 상태
    IN_LOBBY("게임 대기실"),
    IN_TEAM_BUILDER("팀 구성 중"),
    IN_CHAMPION_SELECT("챔피언 선택 중"),

    // 게임 진행 상태
    IN_GAME_LOADING("게임 로딩 중"),
    IN_GAME("게임 진행 중"),
    IN_GAME_FINISHED("게임 종료 중"),
    
    // 커스텀 게임 상태
    IN_CUSTOM_LOBBY("커스텀 대기실"),
    IN_CUSTOM_GAME("커스텀 게임 중"),

    // 특수 상태
    IN_TUTORIAL("튜토리얼 진행 중"),
    IN_PRACTICE_TOOL("연습 도구 사용 중"),
    IN_TFT("전략적 팀 전투 중"),
    ;

    private String description;

    MemberStatus(String description) {
        this.description = description;
    }

    public String toLowerCase() {
        return this.name().toLowerCase();
    }

    public boolean isInGame() {
        return this == IN_GAME || this == IN_GAME_LOADING || this == IN_GAME_FINISHED
                || this == IN_CUSTOM_GAME || this == IN_TUTORIAL
                || this == IN_PRACTICE_TOOL || this == IN_TFT;
    }

    public boolean isQueuing() {
        return this == QUEUING_SOLO || this == QUEUING_FLEX
                || this == QUEUING_NORMAL || this == QUEUING_ARAM;
    }

    public boolean isInChampSelect() {
        return this == IN_CHAMPION_SELECT;
    }

    public boolean isAvailableForInvite() {
        return this == ONLINE || this == AWAY || this == IN_LOBBY
                || this == IN_CUSTOM_LOBBY;
    }
}
