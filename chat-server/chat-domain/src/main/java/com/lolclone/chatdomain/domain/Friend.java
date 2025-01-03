package com.lolclone.chatdomain.domain;

import java.util.UUID;

import com.lolclone.chatdomain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 친구 관계 관리(친구 요청, 삭제)
 * 친구 목록 조회
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
public class Friend extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_user_id", nullable = false, columnDefinition = "uuid")
    private Member friend;

    @Column(length = 100)
    private String memo;

    private boolean isBlocked = false;

    private Friend(Member user, Member friend) {
        this.user = user;
        this.friend = friend;
    }

    public static Friend of(Member user, Member friend) {
        return new Friend(user, friend);
    }

    // 친구 차단
    public void block() {
        this.isBlocked = true;
    }

    // 친구 차단 해제
    public void unblock() {
        this.isBlocked = false;
    }

    // 메모 수정
    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public boolean isBlocked() {
        return this.isBlocked;
    }
}
