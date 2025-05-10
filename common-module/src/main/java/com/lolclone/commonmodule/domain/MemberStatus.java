package com.lolclone.commonmodule.domain;

import lombok.Getter;

@Getter
public enum MemberStatus {
    // 기본상태
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    AWAY("자리 비움")
    ;

    private final String description;

    MemberStatus(String description) {
        this.description = description;
    }
}
