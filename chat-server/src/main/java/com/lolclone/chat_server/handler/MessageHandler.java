package com.lolclone.chat_server.handler;

import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lolclone.chat_server.domain.Message;
import com.lolclone.chat_server.dto.notification.NotificationMessage;
import com.lolclone.chat_server.dto.response.MessageDto;
import com.lolclone.chat_server.exception.common.InternalServerException;
import com.lolclone.chat_server.exception.domain.ExceptionType;
import com.lolclone.chat_server.dto.notification.GameInvitationDTO;
import com.lolclone.chat_server.dto.event.UserStatusEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    public void sendMessageToUser(Message message) {
        try { 
            messagingTemplate.convertAndSendToUser(
                message.getReceiverId().toString(),
                "/queue/messages",
                MessageDto.from(message)
            );
        } catch (Exception e) {
            throw new InternalServerException(ExceptionType.PRIVATE_MESSAGE_SEND_ERROR);
        }
    }
    
    public void sendNotificationToUser(UUID receiverId, NotificationMessage notification) {
        try {
            messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/notifications",
                notification
            );
        } catch (Exception e) {
            throw new InternalServerException(ExceptionType.MESSAGING_ERROR);
        }
    }
    
    public void sendGameInvitation(UUID receiverId, GameInvitationDTO invitation) {
        try {
            messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/invitations",
                invitation
            );
        } catch (Exception e) {
            throw new InternalServerException(ExceptionType.MESSAGING_ERROR);
        }
    }
    
    public void sendStatusUpdate(Long receiverId, UserStatusEvent event) {
        String destination = "/queue/status/" + receiverId;
        messagingTemplate.convertAndSend(destination, event);
    }
} 