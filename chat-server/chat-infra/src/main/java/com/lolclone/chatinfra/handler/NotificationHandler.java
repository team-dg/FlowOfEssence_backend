package com.lolclone.chatinfra.handler;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.domain.Notification;
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
     */
    @Async
    public void sendNotification(final Member receiver, final Notification notification) {
        try {
            String destination = NOTIFICATION_DESTINATION + receiver.getId();
            NotificationDto notificationDto = NotificationDto.from(notification);
            messagingTemplate.convertAndSend(destination, notificationDto);
        } catch (Exception e) {
            // TODO: 알림 전송 실패 시 재시도 로직 작성
            throw new InternalServerException(ExceptionType.NOTIFICATION_SEND_ERROR);
        }
    }
}

