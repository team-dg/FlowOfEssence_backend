package com.lolclone.chatdomain.domain;

import lombok.Getter;

@Getter
public enum ReportReason {
    VERBAL_ABUSE("언어 폭력"),
    HATE_SPEECH("혐오 발언"),
    NEGATIVE_ATTITUDE("부정적인 태도"),
    AFK("게임 포기/자리 비움"),
    INTENTIONAL_FEEDING("의도적인 피딩"),
    CHEATING("부정 행위"),
    INAPPROPRIATE_NAME("부적절한 소환사명"),
    OTHER("기타");

    private final String description;

    ReportReason(String description) {
        this.description = description;
    }
}
