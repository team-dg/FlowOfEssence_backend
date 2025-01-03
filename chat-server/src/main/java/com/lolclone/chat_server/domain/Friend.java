package com.lolclone.chat_server.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.lolclone.chat_server.common.domain.BaseTimeEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "friends",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_friend",
            columnNames = {"user_id", "friend_user_id"}
        )
    },
    indexes = {
        @Index(name = "idx_user_friend", columnList = "user_id, friend_user_id")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Friend extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "friend_id", columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_user_id", nullable = false)
    private User friend;

    @Column(length = 500)
    private String memo;

    private boolean isBlocked = false;
    
    private Friend(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }
    
    public static Friend of(User user, User friend) {
        return new Friend(user, friend);
    }
    
    public UUID getUserId() {
        return user.getId();
    }
    
    public UUID getFriendId() {
        return friend.getId();
    }
    
    public String getFriendName() {
        return friend.getNickname();
    }
    
    public UserStatus getStatus() {
        return friend.getStatus();
    }

    public void setBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}