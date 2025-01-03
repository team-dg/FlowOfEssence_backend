package com.lolclone.chatdomain.domain;

import lombok.Getter;

@Getter
public enum FriendStatus {
    PENDING("대기"),
    ACCEPTED("수락됨"),
    REJECTED("거절됨");

    private final String description;

    FriendStatus(String description) {
        this.description = description;
    }
}
