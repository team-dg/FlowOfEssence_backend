package com.lolclone.chatdomain.domain;

import com.lolclone.chatdomain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 친구 요청 관리(수락, 거절)
 * 친구 요청 목록 조회
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friend_requests")
public class FriendRequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status = FriendStatus.PENDING;

    private FriendRequest(Member requester, Member receiver) {
        this.requester = requester;
        this.receiver = receiver;
    }

    public static FriendRequest create(Member requester, Member receiver) {
        return new FriendRequest(requester, receiver);
    }

    // 비즈니스 메서드
    /**
     * 친구 요청 수락
     */
    public Friend accept() {
        validatePendingStatus();
        this.status = FriendStatus.ACCEPTED;
        return Friend.of(requester, receiver);
    }

    /**
     * 친구 요청 거절
     */
    public void reject() {
        validatePendingStatus();
        this.status = FriendStatus.REJECTED;
    }

    private void validatePendingStatus() {
        if (this.status != FriendStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 친구 요청입니다.");
        }
    }

    public boolean isPending() {
        return this.status == FriendStatus.PENDING;
    }
}
