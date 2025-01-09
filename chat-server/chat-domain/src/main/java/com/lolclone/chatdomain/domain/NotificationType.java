package com.lolclone.chatdomain.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    FRIEND_REQUEST("친구 요청"),
    FRIEND_REQUEST_ACCEPTED("친구 요청 수락"),
    FRIEND_REQUEST_REJECTED("친구 요청 거절"),
    GAME_INVITE("게임 초대"),
    GAME_INVITE_ACCEPTED("게임 초대 수락"),
    GAME_INVITE_REJECTED("게임 초대 거절");

    private final String description;
}
