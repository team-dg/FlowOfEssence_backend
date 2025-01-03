package com.lolclone.chat_server.dto.notification;

import java.util.UUID;

public record NotificationMessage(
    NotificationType type,
    UUID senderId,
    String senderName,
    String message
) {
    public static NotificationMessage ofFriendRequest(UUID senderId, String senderName) {
        return new NotificationMessage(
            NotificationType.FRIEND_REQUEST,
            senderId,
            senderName,
            String.format("%s님이 친구 요청을 보냈습니다.", senderName)
        );
    }
    
    public static NotificationMessage ofFriendAccept(UUID accepterId, String accepterName) {
        return new NotificationMessage(
            NotificationType.FRIEND_ACCEPT,
            accepterId,
            accepterName,
            String.format("%s님이 친구 요청을 수락했습니다.", accepterName)
        );
    }
} 