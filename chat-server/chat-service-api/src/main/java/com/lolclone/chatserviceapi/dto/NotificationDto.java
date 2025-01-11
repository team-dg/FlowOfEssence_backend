package com.lolclone.chatserviceapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lolclone.chatdomain.domain.Notification;
import com.lolclone.chatdomain.domain.NotificationType;

import lombok.Builder;

public record NotificationDto(
    Long id,
    NotificationType type,
    String message,
    UUID senderId,
    String senderNickname,
    LocalDateTime createdAt
) {
    @Builder
    public NotificationDto(
            Long id, NotificationType type, String message, 
            UUID senderId, String senderNickname, 
            LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.createdAt = createdAt;
    }

    public static NotificationDto from(Notification notification, String message) {
        return NotificationDto.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(message)
                .senderId(notification.getSender().getId())
                .senderNickname(notification.getSender().getNickname())
                .createdAt(notification.getCreatedDate())
                .build();
    }
}
