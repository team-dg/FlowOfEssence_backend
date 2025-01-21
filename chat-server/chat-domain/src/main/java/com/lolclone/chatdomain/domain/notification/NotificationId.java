package com.lolclone.chatdomain.domain.notification;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationId implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long value;

    private NotificationId(Long value) {
        this.value = value;
    }

    public static NotificationId newId() {
        return new NotificationId();
    }

    public Long getValue() {
        return value;
    }
}
