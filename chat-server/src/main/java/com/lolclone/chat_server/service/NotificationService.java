package com.lolclone.chat_server.service;

import com.lolclone.chat_server.dto.notification.GameInvitationDTO;
import com.lolclone.chat_server.dto.notification.NotificationMessage;
import com.lolclone.chat_server.exception.common.ForbiddenException;
import com.lolclone.chat_server.exception.domain.ExceptionType;
import com.lolclone.chat_server.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final UserService userService;
    private final MessageHandler messageHandler;
    
    /**
     * 친구 요청 알림을 전송합니다.
     */
    public void sendFriendRequestNotification(Long receiverId, Long senderId) {
        String senderName = userService.getUsername(senderId);
        NotificationMessage notification = NotificationMessage.ofFriendRequest(senderId, senderName);
        messageHandler.sendNotificationToUser(receiverId, notification);
    }
    
    /**
     * 친구 요청 수락 알림을 전송합니다.
     */
    public void sendFriendAcceptNotification(Long receiverId, Long accepterId) {
        String accepterName = userService.getUsername(accepterId);
        NotificationMessage notification = NotificationMessage.ofFriendAccept(accepterId, accepterName);
        messageHandler.sendNotificationToUser(receiverId, notification);
    }
    
    public void sendGameInvitation(Long senderId, Long receiverId, Long roomId, String message, boolean isBlocked) {
        // 차단 여부 확인
        if (isBlocked) {
            throw new ForbiddenException(ExceptionType.BLOCKED_USER);
        }

        GameInvitationDTO invitation = GameInvitationDTO.of(senderId, roomId, message);
        messageHandler.sendGameInvitation(receiverId, invitation);
    }
} 