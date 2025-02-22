package com.lolclone.chatdomain.domain.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationType {
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private Type type;

    private NotificationType(Type type) {
        this.type = type;
    }

    // 정적 팩토리 메서드
    public static NotificationType friendRequest() {
        return new NotificationType(Type.FRIEND_REQUEST);
    }

    public static NotificationType friendRequestAccepted() {
        return new NotificationType(Type.FRIEND_REQUEST_ACCEPTED);
    }

    public static NotificationType friendRequestRejected() {
        return new NotificationType(Type.FRIEND_REQUEST_REJECTED);
    }

    public static NotificationType gameInvite() {
        return new NotificationType(Type.GAME_INVITE);
    }

    public static NotificationType gameInviteAccepted() {
        return new NotificationType(Type.GAME_INVITE_ACCEPTED);
    }

    public static NotificationType gameInviteRejected() {
        return new NotificationType(Type.GAME_INVITE_REJECTED);
    }

    public static NotificationType gameInviteExpired() {
        return new NotificationType(Type.GAME_INVITE_EXPIRED);
    }

    public static NotificationType gameInviteCanceled() {
        return new NotificationType(Type.GAME_INVITE_CANCELED);
    }

    // 비즈니스 로직 메서드
    public boolean isFriendRequest() {
        return this.type == Type.FRIEND_REQUEST;
    }

    public boolean isFriendRequestAccepted() {
        return this.type == Type.FRIEND_REQUEST_ACCEPTED;
    }

    public boolean isFriendRequestRejected() {
        return this.type == Type.FRIEND_REQUEST_REJECTED;
    }

    public boolean isGameInvite() {
        return this.type == Type.GAME_INVITE;
    }

    public boolean isGameInviteAccepted() {
        return this.type == Type.GAME_INVITE_ACCEPTED;
    }

    public boolean isGameInviteRejected() {
        return this.type == Type.GAME_INVITE_REJECTED;
    }

    public String getDescription() {
        return this.type.getDescription();
    }

    @Getter
    public enum Type {
        FRIEND_REQUEST("친구 요청"),
        FRIEND_REQUEST_ACCEPTED("친구 요청 수락"),
        FRIEND_REQUEST_REJECTED("친구 요청 거절"),
        GAME_INVITE("게임 초대"),
        GAME_INVITE_ACCEPTED("게임 초대 수락"),
        GAME_INVITE_REJECTED("게임 초대 거절"),
        GAME_INVITE_EXPIRED("게임 초대 만료"),
        GAME_INVITE_CANCELED("게임 초대 취소"),
        GAME_STARTED("게임 시작"), 
        GAME_FINISHED("게임 종료"),
        TEAM_CHAT_MENTION("팀 채팅 멘션");

        private final String description;

        Type(String description) {
            this.description = description;
        }
    }
}
