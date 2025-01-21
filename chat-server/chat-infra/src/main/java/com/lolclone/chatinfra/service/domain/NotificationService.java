package com.lolclone.chatinfra.service.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.friendrequest.FriendRequest;
import com.lolclone.chatdomain.domain.gameinvite.GameInvite;
import com.lolclone.chatdomain.domain.notification.Notification;
import com.lolclone.chatdomain.repository.NotificationRepository;
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

    /**
     * 친구 요청 알림 전송
     */
    @Transactional
    public Notification sendFriendRequestNotification(FriendRequest request) {
        Notification notification = Notification.createFriendRequest(request);
        notificationHandler.sendNotification(notification.getRecipient(), notification);
        return notificationRepository.save(notification);
    }

    /**
     * 친구 요청 수락 알림 전송
     */
    @Transactional
    public Notification sendFriendRequestAcceptedNotification(FriendRequest request) {
        Notification notification = Notification.createFriendRequestAccepted(request);
        notificationHandler.sendNotification(notification.getSender(), notification);
        return notificationRepository.save(notification);
    }

    /**
     * 친구 요청 거절 알림 전송
     */
    @Transactional
    public Notification sendFriendRequestRejectedNotification(FriendRequest request) {
        Notification notification = Notification.createFriendRequestRejected(request);
        notificationHandler.sendNotification(notification.getSender(), notification);
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification createGameInviteNotification(GameInvite gameInvite) {
        return notificationRepository.save(Notification.createGameInvite(gameInvite));
    }

    @Transactional
    public Notification createGameInviteAcceptedNotification(GameInvite gameInvite) {
        return notificationRepository.save(Notification.createGameInviteAccepted(gameInvite));
    }

    @Transactional
    public Notification createGameInviteRejectedNotification(GameInvite gameInvite) {
        return notificationRepository.save(Notification.createGameInviteRejected(gameInvite));
    }

    @Transactional
    public Notification createGameInviteExpiredNotification(GameInvite gameInvite) {
        return notificationRepository.save(Notification.createGameInviteExpired(gameInvite));
    }

    @Transactional
    public Notification createGameInviteCanceledNotification(GameInvite gameInvite) {
        return notificationRepository.save(Notification.createGameInviteCanceled(gameInvite));
    }
}
