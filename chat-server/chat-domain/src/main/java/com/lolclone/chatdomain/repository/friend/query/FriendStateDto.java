package com.lolclone.chatdomain.repository.friend.query;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lolclone.chatdomain.domain.MemberStatus;
import com.querydsl.core.annotations.QueryProjection;

public record FriendStateDto(
    MemberStatus status,
    String gameInfo,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastActiveTime,
    boolean isOnline
) {
    @QueryProjection
    public FriendStateDto(MemberStatus status, String gameInfo, LocalDateTime lastActiveTime, boolean isOnline) {
        this.status = status;
        this.gameInfo = gameInfo;
        this.lastActiveTime = lastActiveTime;
        this.isOnline = isOnline;
    }
}
