package com.lolclone.chatdomain.domain;

import lombok.Getter;

@Getter
public enum MemberState {
    PENDING("대기 상태"),
    ACTIVE("활성화 상태"),
    DELETED("삭제된 상태")
    ;

    private final String description;

    private MemberState(String description) {
        this.description = description;
    }
}
