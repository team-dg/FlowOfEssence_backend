package com.lolclone.chatdomain.repository.friend.query;

import java.util.Set;

import com.querydsl.core.annotations.QueryProjection;

public record FriendRelationDto(
    String memo,
    Set<String> tags,
    boolean isBlocked
) {
    @QueryProjection
    public FriendRelationDto(String memo, Set<String> tags, boolean isBlocked) {
        this.memo = memo;
        this.tags = tags;
        this.isBlocked = isBlocked;
    }
}
