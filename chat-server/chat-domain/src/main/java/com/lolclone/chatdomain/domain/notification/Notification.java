package com.lolclone.chatdomain.domain.notification;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import com.lolclone.chatdomain.domain.common.BaseTimeEntity;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

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

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Builder
    private Notification(
        NotificationType type, 
        Member recipient, 
        Member sender,
        String templateKey,
        Map<String, String> variables,
        FriendRequest friendRequest,
        GameInvite gameInvite,
        Duration ttl) {
        this.type = type;
        this.recipient = recipient;
        this.sender = sender;
        this.content = NotificationContent.of(templateKey, variables);
        this.status = NotificationStatus.unread();
        this.gameInvite = gameInvite;
        this.friendRequest = friendRequest;
        this.gameInvite = gameInvite;
        this.expiresAt = LocalDateTime.now().plus(ttl);
    }

    // 정적 팩토리 메서드
    public static Notification createFriendRequest(FriendRequest request) {
        return Notification.builder()
            .type(NotificationType.friendRequest())
            .recipient(request.getReceiver())
            .sender(request.getRequester())
            .templateKey("friend.request")
            .variables(Map.of("sender", request.getRequester().getNickname()))
            .friendRequest(request)
            .ttl(Duration.ofDays(7)) // 7일 후 만료
            .build();
    }

    public static Notification createFriendRequestAccepted(FriendRequest request) {
        return Notification.builder()
            .type(NotificationType.friendRequestAccepted())
            .recipient(request.getRequester())
            .sender(request.getReceiver())
            .templateKey("friend.request.accepted")
            .variables(Map.of("accepter", request.getReceiver().getNickname()))
            .friendRequest(request)
            .ttl(Duration.ofDays(7))
            .build();
    }

    public static Notification createFriendRequestRejected(FriendRequest request) {
        return Notification.builder()
            .type(NotificationType.friendRequestRejected())
            .recipient(request.getRequester())
            .sender(request.getReceiver())
            .templateKey("friend.request.rejected")
            .variables(Map.of("rejecter", request.getReceiver().getNickname()))
            .friendRequest(request)
            .ttl(Duration.ofDays(7))
            .build();
    }

    // 정적 팩토리 메서드 (GameInvite 관련)
    public static Notification createGameInvite(GameInvite invite) {
        return Notification.builder()
            .type(NotificationType.gameInvite())
            .recipient(invite.getInvitee())
            .sender(invite.getInviter())
            .templateKey("game.invite")
            .variables(Map.of(
                "inviter", invite.getInviter().getNickname(),
                "gameType", invite.getGameType().getDisplayName()
            ))
            .gameInvite(invite)
            .ttl(Duration.ofMinutes(30)) // 30분 후 만료
            .build();
    }

    public static Notification createGameInviteAccepted(GameInvite invite) {
        return Notification.builder()
            .type(NotificationType.gameInviteAccepted())
            .recipient(invite.getInviter())
            .sender(invite.getInvitee())
            .templateKey("game.invite.accepted")
            .variables(Map.of("invitee", invite.getInvitee().getNickname()))
            .gameInvite(invite)
            .ttl(Duration.ofDays(1))
            .build();
    }

    public static Notification createGameInviteRejected(GameInvite invite) {
        return Notification.builder()
            .type(NotificationType.gameInviteRejected())
            .recipient(invite.getInviter())
            .sender(invite.getInvitee())
            .templateKey("game.invite.rejected")
            .variables(Map.of("invitee", invite.getInvitee().getNickname()))
            .gameInvite(invite)
            .ttl(Duration.ofDays(1))
            .build();
    }

    public static Notification createGameInviteExpired(GameInvite invite) {
        return Notification.builder()
            .type(NotificationType.gameInviteExpired())
            .recipient(invite.getInvitee())
            .sender(invite.getInviter())
            .templateKey("game.invite.expired")
            .variables(Map.of("inviter", invite.getInviter().getNickname()))
            .gameInvite(invite)
            .ttl(Duration.ofDays(1))
            .build();
    }

    public static Notification createGameInviteCanceled(GameInvite invite) {
        return Notification.builder()
            .type(NotificationType.gameInviteCanceled())
            .recipient(invite.getInvitee())
            .sender(invite.getInviter())
            .templateKey("game.invite.canceled")
            .variables(Map.of("inviter", invite.getInviter().getNickname()))
            .gameInvite(invite)
            .ttl(Duration.ofDays(1))
            .build();
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
}
