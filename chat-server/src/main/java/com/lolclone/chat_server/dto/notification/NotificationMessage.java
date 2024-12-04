package com.lolclone.chat_server.dto.notification;

public record NotificationMessage(
    NotificationType type,
    Long senderId,
    String senderName,
    String message
) {
    public static NotificationMessage ofFriendRequest(Long senderId, String senderName) {
        return new NotificationMessage(
            NotificationType.FRIEND_REQUEST,
            senderId,
            senderName,
            String.format("%s님이 친구 요청을 보냈습니다.", senderName)
        );
    }
    
    public static NotificationMessage ofFriendAccept(Long accepterId, String accepterName) {
        return new NotificationMessage(
            NotificationType.FRIEND_ACCEPT,
            accepterId,
            accepterName,
            String.format("%s님이 친구 요청을 수락했습니다.", accepterName)
        );
    }
} 