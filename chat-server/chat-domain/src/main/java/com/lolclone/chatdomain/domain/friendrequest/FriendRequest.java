package com.lolclone.chatdomain.domain.friendrequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.lolclone.chatdomain.domain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.friend.Friend;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.exception.InvalidFriendRequestException;
import com.lolclone.chatdomain.exception.InvalidFriendRequestStatusException;
import com.lolclone.chatdomain.exception.UnauthorizedFriendRequestException;

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
@Table(name = "friend_requests",
    indexes = {
        @Index(name = "idx_receiver_status", columnList = "receiver_id, status")
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "request_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Embedded
    private FriendRequestStatus status; // 상태를 값 객체로 분리

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    private FriendRequest(Member requester, Member receiver) {
        validateRequest(requester, receiver);
        this.requester = requester;
        this.receiver = receiver;
        this.status = FriendRequestStatus.pending();
        this.processedAt = null;
    }

    // 정적 팩토리 메서드
    public static FriendRequest create(Member requester, Member receiver) {
        return new FriendRequest(requester, receiver);
    }

    // 이는 서비스 로직이므로 밖으로 빼기
    // public static FriendRequest create(Member requester, Member receiver) {
    //     FriendRequest request = new FriendRequest(requester, receiver);
    //     notificationService.send(
    //         receiver, 
    //         NotificationType.FRIEND_REQUEST, 
    //         requester.getNickname() + "님이 친구 요청을 보냈습니다."
    //     );
    //     return request;
    // }

    // 비즈니스 메서드
    public List<Friend> accept() {
        validatePendingStatus();
        this.status = this.status.accept();
        updateProcessedTime();
        return List.of(
            Friend.create(requester, receiver), 
            Friend.create(receiver, requester)
        );
    }

    public void reject() {
        validatePendingStatus();
        this.status = this.status.reject();
        updateProcessedTime();
    }

    public void cancel() {
        validatePendingStatus();
        validateRequester(requester);
        this.status = this.status.cancel();
        updateProcessedTime();
    }

    // 검증 메서드
    private static void validateRequest(Member requester, Member receiver) {
        if (requester.equals(receiver)) {
            throw new InvalidFriendRequestException("자기 자신에게 친구 요청을 보낼 수 없습니다.");
        }
    }

    private void validatePendingStatus() {
        if (!this.status.isPending()) {
            throw new InvalidFriendRequestStatusException("이미 처리된 친구 요청입니다.", this.id);
        }
    }

    private void validateRequester(Member member) {
        if (!this.requester.equals(member)) {
            throw new UnauthorizedFriendRequestException("친구 요청을 취소할 권한이 없습니다.", this.id);
        }
    }

    // 상태 확인 메서드
    public boolean isPending() {
        return this.status.isPending();
    }

    public boolean isAccepted() {
        return this.status.isAccepted();
    }

    public boolean isRejected() {
        return this.status.isRejected();
    }

    public boolean isCanceled() {
        return this.status.isCanceled();
    }

    public boolean involvesUser(Member user) {
        return this.requester.equals(user) || this.receiver.equals(user);
    }

    public void updateProcessedTime() {
        this.processedAt = LocalDateTime.now();
    }

    public boolean isProcessed() {
        return processedAt != null;
    }
}
