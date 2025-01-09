package com.lolclone.chatdomain.domain;

import lombok.Getter;

@Getter
public enum MessageType {
    TEXT("일반 텍스트"),
    SYSTEM("시스템 메시지"),
    WHISPER("귓속말")
    ;

    private final String description;

    MessageType(String description) {
        this.description = description;
    }
}
