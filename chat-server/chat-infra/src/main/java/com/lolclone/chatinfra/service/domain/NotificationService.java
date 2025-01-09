package com.lolclone.chatinfra.service.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.FriendRequest;
import com.lolclone.chatdomain.domain.Notification;
import com.lolclone.chatdomain.repository.NotificationRepository;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;
import com.lolclone.chatinfra.handler.NotificationHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationHandler notificationHandler;

    public Notification getOrThrow(final Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.NOTIFICATION_NOT_FOUND));
    }

    /**
     * 친구 요청 알림 전송
     */
    @Transactional
    public void sendFriendRequestNotification(FriendRequest request) {
        Notification notification = Notification.createFriendRequestNotification(request);
        notificationRepository.save(notification);
        notificationHandler.sendNotification(notification.getRecipient(), notification);
    }

    /**
     * 친구 요청 수락 알림 전송
     */
    @Transactional
    public void sendFriendRequestAcceptedNotification(FriendRequest request) {
        Notification notification = Notification.createFriendRequestAcceptedNotification(request);
        notificationRepository.save(notification);
        notificationHandler.sendNotification(notification.getSender(), notification);
    }

    /**
     * 친구 요청 거절 알림 전송
     */
    @Transactional
    public void sendFriendRequestRejectedNotification(FriendRequest request) {
        Notification notification = Notification.createFriendRequestRejectedNotification(request);
        notificationRepository.save(notification);
        notificationHandler.sendNotification(notification.getSender(), notification);
    }

    /**
     * 게임 초대 알림 전송
     */
    @Transactional
    public void sendGameInviteNotification(FriendRequest request) {
        Notification notification = Notification.createGameInviteNotification(request);
        notificationRepository.save(notification);
        notificationHandler.sendNotification(notification.getRecipient(), notification);
    }

    /**
     * 게임 초대 수락 알림 전송
     */
    @Transactional
    public void sendGameInviteAcceptedNotification(FriendRequest request) {
        Notification notification = Notification.createGameInviteAcceptedNotification(request);
        notificationRepository.save(notification);
        notificationHandler.sendNotification(notification.getSender(), notification);
    }

    /**
     * 게임 초대 거절 알림 전송
     */
    @Transactional
    public void sendGameInviteRejectedNotification(FriendRequest request) {
        Notification notification = Notification.createGameInviteRejectedNotification(request);
        notificationRepository.save(notification);
        notificationHandler.sendNotification(notification.getSender(), notification);
    }
}
