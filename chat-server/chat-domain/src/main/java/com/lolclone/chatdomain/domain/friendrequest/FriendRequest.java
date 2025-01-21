package com.lolclone.chatdomain.domain.friendrequest;

import com.lolclone.chatdomain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.friend.Friend;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.exception.InvalidFriendRequestException;
import com.lolclone.chatdomain.exception.InvalidFriendRequestStatusException;
import com.lolclone.chatdomain.exception.InvalidFriendRequestTypeException;
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
@Table(name = "friend_requests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequest extends BaseTimeEntity {
    @EmbeddedId
    private FriendRequestId id; // ID를 값 객체로 분리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Embedded
    private FriendRequestStatus status; // 상태를 값 객체로 분리

    @Embedded
    private FriendRequestMetadata metadata; // 메타데이터를 값 객체로 분리

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private FriendRequestType requestType = FriendRequestType.FRIEND;

    private FriendRequest(Member requester, Member receiver) {
        validateRequest(requester, receiver);
        this.id = FriendRequestId.newId();
        this.requester = requester;
        this.receiver = receiver;
        this.status = FriendRequestStatus.pending();
        this.metadata = FriendRequestMetadata.init();
    }

    // 정적 팩토리 메서드
    public static FriendRequest create(Member requester, Member receiver) {
        return new FriendRequest(requester, receiver);
    }

    // 비즈니스 메서드
    public Friend accept() {
        validatePendingStatus();
        this.status = this.status.accept();
        this.metadata = this.metadata.updateProcessedTime();
        return Friend.create(requester, receiver);
    }

    public void reject() {
        validatePendingStatus();
        this.status = this.status.reject();
        this.metadata = this.metadata.updateProcessedTime();
    }

    public void cancel() {
        validatePendingStatus();
        validateRequester(requester);
        this.status = this.status.cancel();
        this.metadata = this.metadata.updateProcessedTime();
    }

    public static FriendRequest createGameInvite(Member requester, Member receiver) {
        FriendRequest request = new FriendRequest(requester, receiver);
        request.requestType = FriendRequestType.GAME_INVITE;
        return request;
    }

    public Friend acceptGameInvite() {
        validateGameInvite();
        validatePendingStatus();
        this.status = this.status.accept();
        return Friend.create(requester, receiver);
    }

    public void rejectGameInvite() {
        validateGameInvite();
        validatePendingStatus();
        this.status = this.status.reject();
    }

    private void validateGameInvite() {
        if (this.requestType != FriendRequestType.GAME_INVITE) {
            throw new InvalidFriendRequestTypeException("게임 초대 요청이 아닙니다.");
        }
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
}
