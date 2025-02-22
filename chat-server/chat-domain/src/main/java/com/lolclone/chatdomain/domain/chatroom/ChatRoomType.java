package com.lolclone.chatdomain.domain.chatroom;

import lombok.Getter;

@Getter
public enum ChatRoomType {
    PERSONAL("1:1 채팅"),
    GROUP("그룹 채팅"),
    GAME_LOBBY("게임 대기실"),
    GAME_SESSION("게임 중/종료")
    ;

    private final String description;

    ChatRoomType(String description) {
        this.description = description;
    }
}
