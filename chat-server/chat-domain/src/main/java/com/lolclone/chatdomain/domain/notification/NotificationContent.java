package com.lolclone.chatdomain.domain.notification;

import com.lolclone.chatdomain.exception.InvalidNotificationContentException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationContent {
    @Column(name = "content", nullable = false)
    private String value;

    private NotificationContent(String value) {
        validateContent(value);
        this.value = value;
    }

    public static NotificationContent of(String content) {
        return new NotificationContent(content);
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidNotificationContentException("알림 내용은 비어있을 수 없습니다.");
        }
        if (content.length() > 200) {
            throw new InvalidNotificationContentException("알림 내용은 200자를 초과할 수 없습니다.");
        }
    }
}
