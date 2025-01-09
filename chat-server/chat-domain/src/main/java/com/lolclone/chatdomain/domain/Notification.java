package com.lolclone.chatdomain.domain;

import com.lolclone.chatdomain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notifications")
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false, columnDefinition = "uuid")
    private Member recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", columnDefinition = "uuid")
    private Member sender;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    // 친구 요청 알림인 경우
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_request_id")
    private FriendRequest friendRequest;

    @Builder
    public Notification(NotificationType type, Member recipient, Member sender, String content, FriendRequest friendRequest) {
        this.type = type;
        this.recipient = recipient;
        this.sender = sender;
        this.content = content;
        this.isRead = false;
        this.friendRequest = friendRequest;
    }

    // 정적 팩토리 메서드
    public static Notification createFriendRequestNotification(FriendRequest request) {
        return Notification.builder()
                .type(NotificationType.FRIEND_REQUEST)
                .recipient(request.getReceiver())
                .sender(request.getRequester())
                .content(String.format("%s님이 친구 요청을 보냈습니다.", request.getRequester().getNickname()))
                .friendRequest(request)
                .build();
    }

    public static Notification createFriendRequestAcceptedNotification(FriendRequest request) {
        return Notification.builder()
                .type(NotificationType.FRIEND_REQUEST_ACCEPTED)
                .recipient(request.getRequester()) // 요청자에게 알림
                .sender(request.getReceiver()) // 수락한 사람이 발신자
                .content(String.format("%s님이 친구 요청을 수락했습니다.", request.getReceiver().getNickname()))
                .friendRequest(request)
                .build();
    }

    public static Notification createFriendRequestRejectedNotification(FriendRequest request) {
        return Notification.builder()
                .type(NotificationType.FRIEND_REQUEST_REJECTED)
                .recipient(request.getRequester()) // 요청자에게 알림
                .sender(request.getReceiver()) // 거절한 사람이 발신자
                .content(String.format("%s님이 친구 요청을 거절했습니다.", request.getReceiver().getNickname()))
                .friendRequest(request)
                .build();
    }

    public static Notification createGameInviteNotification(FriendRequest request) {
        return Notification.builder()
                .type(NotificationType.GAME_INVITE)
                .recipient(request.getReceiver())
                .sender(request.getRequester())
                .content(String.format("%s님이 게임에 초대하셨습니다.", request.getRequester().getNickname()))
                .build();
    }

    public static Notification createGameInviteAcceptedNotification(FriendRequest request) {
        return Notification.builder()
                .type(NotificationType.GAME_INVITE_ACCEPTED)
                .recipient(request.getRequester())
                .sender(request.getReceiver())
                .content(String.format("%s님이 게임에 수락하셨습니다.", request.getReceiver().getNickname()))
                .build();
    }

    public static Notification createGameInviteRejectedNotification(FriendRequest request) {
        return Notification.builder()
                .type(NotificationType.GAME_INVITE_REJECTED)
                .recipient(request.getRequester())
                .sender(request.getReceiver())
                .content(String.format("%s님이 게임에 거절하셨습니다.", request.getReceiver().getNickname()))
                .build();
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public boolean isGameInviteNotification() {
        return this.type == NotificationType.GAME_INVITE;
    }

    public boolean isFriendRequestNotification() {
        return this.type == NotificationType.FRIEND_REQUEST;
    }
}
