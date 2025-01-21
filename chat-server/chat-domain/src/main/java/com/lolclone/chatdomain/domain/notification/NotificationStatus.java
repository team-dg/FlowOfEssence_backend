package com.lolclone.chatdomain.domain.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationStatus {
    @Column(name = "is_read", nullable = false)
    private boolean read;

    private NotificationStatus(boolean read) {
        this.read = read;
    }

    public static NotificationStatus unread() {
        return new NotificationStatus(false);
    }

    public NotificationStatus markAsRead() {
        return new NotificationStatus(true);
    }

    public boolean isRead() {
        return read;
    }
}
