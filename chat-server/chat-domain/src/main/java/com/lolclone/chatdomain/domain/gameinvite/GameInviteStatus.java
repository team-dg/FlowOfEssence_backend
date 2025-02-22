package com.lolclone.chatdomain.domain.gameinvite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameInviteStatus {
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    private GameInviteStatus(Status status) {
        this.status = status;
    }

    public static GameInviteStatus pending() {
        return new GameInviteStatus(Status.PENDING);
    }

    public GameInviteStatus accept() {
        return new GameInviteStatus(Status.ACCEPTED);
    }

    public GameInviteStatus reject() {
        return new GameInviteStatus(Status.REJECTED);
    }

    public GameInviteStatus cancel() {
        return new GameInviteStatus(Status.CANCELED);
    }

    public GameInviteStatus expire() {
        return new GameInviteStatus(Status.EXPIRED);
    }

    public boolean isPending() {
        return this.status == Status.PENDING;
    }

    public boolean isAccepted() {
        return this.status == Status.ACCEPTED;
    }

    public boolean isRejected() {
        return this.status == Status.REJECTED;
    }

    public boolean isCanceled() {
        return this.status == Status.CANCELED;
    }

    public boolean isExpired() {
        return this.status == Status.EXPIRED;
    }

    @Getter
    public enum Status {
        PENDING("대기중"),
        ACCEPTED("수락됨"),
        REJECTED("거절됨"),
        CANCELED("취소됨"),
        EXPIRED("만료됨");

        private final String description;

        Status(String description) {
            this.description = description;
        }
    }
}
