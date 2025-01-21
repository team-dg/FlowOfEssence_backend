
package com.lolclone.chatdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.notification.Notification;
import com.lolclone.chatdomain.domain.notification.NotificationId;

public interface NotificationRepository extends JpaRepository<Notification, NotificationId> {
    
}
