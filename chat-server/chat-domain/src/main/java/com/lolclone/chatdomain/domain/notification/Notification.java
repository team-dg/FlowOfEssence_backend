package com.lolclone.chatdomain.domain.notification;

import com.lolclone.chatdomain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.friendrequest.FriendRequest;
import com.lolclone.chatdomain.domain.gameinvite.GameInvite;
import com.lolclone.chatdomain.domain.member.Member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {
    @EmbeddedId
    private NotificationId id; // ID를 값 객체로 분리

    @Embedded
    private NotificationContent content; // 내용을 값 객체로 분리

    @Embedded
    private NotificationStatus status; // 상태 정보를 값 객체로 분리

    @Embedded
    private NotificationType type; // 타입을 값 객체로 분리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Member recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_request_id")
    private FriendRequest friendRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_invite_id")
    private GameInvite gameInvite;

    @Builder
    private Notification(NotificationType type, Member recipient, Member sender,
            String content, FriendRequest friendRequest) {
        this.id = NotificationId.newId();
        this.type = type;
        this.recipient = recipient;
        this.sender = sender;
        this.content = NotificationContent.of(content);
        this.status = NotificationStatus.unread();
        this.friendRequest = friendRequest;
    }

    @Builder
    private Notification(NotificationType type, Member recipient, Member sender,
            String content, GameInvite gameInvite) {
        this.id = NotificationId.newId();
        this.type = type;
        this.recipient = recipient;
        this.sender = sender;
        this.content = NotificationContent.of(content);
        this.status = NotificationStatus.unread();
        this.gameInvite = gameInvite;
    }

    // 정적 팩토리 메서드
    public static Notification createFriendRequest(FriendRequest request) {
        return new Notification(
                NotificationType.friendRequest(),
                request.getReceiver(),
                request.getRequester(),
                createFriendRequestContent(request.getRequester()).getValue(),
                request);
    }

    public static Notification createFriendRequestAccepted(FriendRequest request) {
        return new Notification(
                NotificationType.friendRequestAccepted(),
                request.getRequester(),
                request.getReceiver(),
                createFriendRequestAcceptedContent(request.getReceiver()).getValue(),
                request);
    }

    public static Notification createFriendRequestRejected(FriendRequest request) {
        return new Notification(
                NotificationType.friendRequestRejected(),
                request.getRequester(),
                request.getReceiver(),
                createFriendRequestRejectedContent(request.getReceiver()).getValue(),
                request);
    }

    public static Notification createGameInvite(GameInvite invite) {
        return new Notification(
                NotificationType.gameInvite(),
                invite.getInviter(),
                invite.getInvitee(),
                createGameInviteContent(invite.getInviter()).getValue(),
                invite);
    }

    public static Notification createGameInviteAccepted(GameInvite invite) {
        return new Notification(
                NotificationType.gameInviteAccepted(),
                invite.getInvitee(),
                invite.getInviter(),
                createGameInviteAcceptedContent(invite.getInvitee()).getValue(),
                invite);
    }

    public static Notification createGameInviteRejected(GameInvite invite) {
        return new Notification(
                NotificationType.gameInviteRejected(),
                invite.getInvitee(),
                invite.getInviter(),
                createGameInviteRejectedContent(invite.getInvitee()).getValue(),
                invite);
    }

    public static Notification createGameInviteExpired(GameInvite invite) {
        return new Notification(
                NotificationType.gameInviteExpired(),
                invite.getInvitee(),
                invite.getInviter(),
                createGameInviteExpiredContent(invite.getInvitee()).getValue(),
                invite);
    }

    public static Notification createGameInviteCanceled(GameInvite invite) {
        return new Notification(
                NotificationType.gameInviteCanceled(),
                invite.getInviter(),
                invite.getInvitee(),
                createGameInviteCanceledContent(invite.getInviter()).getValue(),
                invite);
    }

    // 상태 확인 메서드
    public boolean isRead() {
        return this.status.isRead();
    }

    public boolean isGameInvite() {
        return this.type.isGameInvite();
    }

    public boolean isFriendRequest() {
        return this.type.isFriendRequest();
    }

    // 컨텐츠 생성 메서드
    private static NotificationContent createFriendRequestContent(Member requester) {
        return NotificationContent.of(String.format("%s님이 친구 요청을 보냈습니다.", requester.getNickname()));
    }

    private static NotificationContent createFriendRequestAcceptedContent(Member accepter) {
        return NotificationContent.of(String.format("%s님이 친구 요청을 수락했습니다.", accepter.getNickname()));
    }

    private static NotificationContent createFriendRequestRejectedContent(Member rejecter) {
        return NotificationContent.of(String.format("%s님이 친구 요청을 거절했습니다.", rejecter.getNickname()));
    }

    private static NotificationContent createGameInviteContent(Member inviter) {
        return NotificationContent.of(String.format("%s님이 게임에 초대하셨습니다.", inviter.getNickname()));
    }

    private static NotificationContent createGameInviteAcceptedContent(Member invitee) {
        return NotificationContent.of(String.format("%s님이 게임 초대를 수락했습니다.", invitee.getNickname()));
    }

    private static NotificationContent createGameInviteRejectedContent(Member invitee) {
        return NotificationContent.of(String.format("%s님이 게임 초대를 거절했습니다.", invitee.getNickname()));
    }

    private static NotificationContent createGameInviteExpiredContent(Member invitee) {
        return NotificationContent.of(String.format("%s님과의 게임 초대가 만료되었습니다.", invitee.getNickname()));
    }

    private static NotificationContent createGameInviteCanceledContent(Member inviter) {
        return NotificationContent.of(String.format("%s님이 게임 초대를 취소했습니다.", inviter.getNickname()));
    }
}
