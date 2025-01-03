package com.lolclone.chat_server.service;

import com.lolclone.chat_server.domain.User;
import com.lolclone.chat_server.dto.event.UserStatusEvent;
import com.lolclone.chat_server.exception.common.NotFoundException;
import com.lolclone.chat_server.exception.domain.ExceptionType;
import com.lolclone.chat_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    public String getUsername(final UUID userId) {
        return userRepository.findUsernameById(userId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.INVALID_RECEIVER));
    }
    
    @Transactional
    public void updateUserStatus(final UserStatusEvent event) {
        final User user = userRepository.findById(event.getUserId())
            .orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
            
        user.updateStatus(event.getStatus());
        if (event.getGameInfo() != null) {
            user.updateGameInfo(event.getGameInfo());
        }
        
        // 친구들에게 상태 변경 알림
        notifyStatusChangeToFriends(event);
    }
    
    private void notifyStatusChangeToFriends(final UserStatusEvent event) {
        eventPublisher.publishEvent(event);
    }
    
    public boolean exists(UUID userId) {
        return userRepository.existsById(userId);
    }
} 