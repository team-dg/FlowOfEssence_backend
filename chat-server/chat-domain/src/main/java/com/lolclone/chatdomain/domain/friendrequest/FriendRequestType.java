package com.lolclone.chatdomain.domain.friendrequest;

import lombok.Getter;

@Getter
public enum FriendRequestType {
    FRIEND("친구 요청"),
    GAME_INVITE("게임 초대");

    private final String description;

    FriendRequestType(String description) {
        this.description = description;
    }
}