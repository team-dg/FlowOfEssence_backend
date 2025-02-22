package com.lolclone.chatdomain.domain;

import lombok.Getter;

@Getter
public enum ReportStatus {
    PENDING("접수됨"),
    PROCESSING("처리중"),
    COMPLETED("처리완료"),
    REJECTED("반려됨");

    private final String description;

    ReportStatus(String description) {
        this.description = description;
    }
}
