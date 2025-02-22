package com.lolclone.chatdomain.repository.friend.query;

import lombok.Builder;

@Builder
public record FriendSortCondition(
    boolean sortByNickname,
    boolean sortByStatus
) {
    public boolean hasAnySortCondition() {
        return sortByNickname || sortByStatus;
    }
}
