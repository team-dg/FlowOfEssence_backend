package com.lolclone.chatdomain.domain.friend;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendshipCriteria {
    private final boolean includeBlocked;
    private final boolean onlineOnly;
    private final LocalDateTime interactedAfter;
    private final Integer minFriendshipScore;
    private final String nicknameKeyword;
    private final String tagKeyword;

    @Builder
    private FriendshipCriteria(
            boolean includeBlocked,
            boolean onlineOnly,
            LocalDateTime interactedAfter,
            Integer minFriendshipScore,
            String nicknameKeyword,
            String tagKeyword) {
        this.includeBlocked = includeBlocked;
        this.onlineOnly = onlineOnly;
        this.interactedAfter = interactedAfter;
        this.minFriendshipScore = minFriendshipScore;
        this.nicknameKeyword = nicknameKeyword;
        this.tagKeyword = tagKeyword;
    }

    public static FriendshipCriteria activeOnly() {
        return FriendshipCriteria.builder()
                .includeBlocked(false)
                .build();
    }

    public static FriendshipCriteria search(String nicknameKeyword, String tagKeyword) {
        return FriendshipCriteria.builder()
                .includeBlocked(false)
                .nicknameKeyword(nicknameKeyword)
                .tagKeyword(tagKeyword)
                .build();
    }
}
