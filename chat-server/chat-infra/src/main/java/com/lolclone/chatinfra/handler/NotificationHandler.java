package com.lolclone.chatinfra.handler;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.domain.notification.Notification;
import com.lolclone.chatdomain.domain.notification.NotificationType;
import com.lolclone.chatinfra.exception.commonexception.InternalServerException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;
import com.lolclone.chatserviceapi.dto.NotificationDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationHandler {
    private final SimpMessagingTemplate messagingTemplate;
    private static final String NOTIFICATION_DESTINATION = "/queue/notifications/";

    /**
     * 특정 사용자에게 알림 전송
     * WebSocket 연결된 사용자에게 직접 알림을 전송
     */
    @Async
    public void sendNotification(final Member receiver, final Notification notification) {
        try {
            String destination = NOTIFICATION_DESTINATION + receiver.getId();
            // 알림 타입에 따른 메시지 생성
            String message = createNotificationMessage(notification.getType(), notification.getSender().getNickname());
            
            NotificationDto notificationDto = NotificationDto.from(notification, message);
            messagingTemplate.convertAndSend(destination, notificationDto);
        } catch (Exception e) {
            // TODO: 알림 전송 실패 시 재시도 로직 작성
            throw new InternalServerException(ExceptionType.NOTIFICATION_SEND_ERROR);
        }
    }

    /**
     * 알림 타입에 따른 메시지 생성
     */
    private String createNotificationMessage(NotificationType type, Object... params) {
        if (type.isFriendRequest()) {
            return String.format("%s님이 친구 요청을 보냈습니다.", params[0]);
        } else if (type.isFriendRequestAccepted()) {
            return String.format("%s님이 친구 요청을 수락했습니다.", params[0]);
        } else if (type.isFriendRequestRejected()) {
            return String.format("%s님이 친구 요청을 거절했습니다.", params[0]);
        } else if (type.isGameInvite()) {
            return String.format("%s님이 게임 초대를 보냈습니다.", params[0]);
        } else if (type.isGameInviteAccepted()) {
            return String.format("%s님이 게임 초대를 수락했습니다.", params[0]);
        } else if (type.isGameInviteRejected()) {
            return String.format("%s님이 게임 초대를 거절했습니다.", params[0]);
        }
        return "알 수 없는 알림";
    }
}

