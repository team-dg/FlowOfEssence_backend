
package com.lolclone.chatdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
}
