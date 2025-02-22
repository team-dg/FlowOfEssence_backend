package com.lolclone.chatdomain.domain.friend;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendId implements Serializable {
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "friend_id")
    private UUID friendId;

    private FriendId(UUID userId, UUID friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public static FriendId of(UUID userId, UUID friendId) {
        return new FriendId(userId, friendId);
    }
}
