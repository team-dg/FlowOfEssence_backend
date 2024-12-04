package com.lolclone.chat_server.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.lolclone.chat_server.domain.Message;
import com.lolclone.chat_server.dto.response.MessageDto;
import com.lolclone.chat_server.exception.common.BadRequestException;
import com.lolclone.chat_server.exception.domain.ExceptionType;
import com.lolclone.chat_server.repository.MessageRepository;
import com.lolclone.chat_server.validator.ChatValidator;
import com.lolclone.chat_server.handler.MessageHandler;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    
    private final MessageRepository messageRepository;
    private final FriendService friendService;
    private final ChatValidator chatValidator;
    private final MessageHandler messageHandler;
    
    /**
     * 친구 관계 검증
     */
    private void validateFriendRelationship(Long userId, Long friendId) {
        if (!friendService.isFriend(userId, friendId)) {
            throw new BadRequestException(ExceptionType.NOT_FRIEND);
        }
    }
    
    public void sendMessage(Long senderId, Long receiverId, String content) {
        // 친구 관계 검증
        validateFriendRelationship(senderId, receiverId);
        
        // 메시지 저장
        Message message = Message.of(senderId, receiverId, content);
        
        // WebSocket을 통해 메시지 전송
        messageHandler.sendMessageToUser(message);
    }
    
    @Transactional(readOnly = true)
    public List<MessageDto> getChatHistory(Long userId, Long friendId) {
        validateFriendRelationship(userId, friendId);
        
        List<Message> messages = messageRepository.findChatHistory(userId, friendId);
        chatValidator.validateChatHistoryExists(messages);
        
        return messages.stream()
            .map(MessageDto::from)
            .collect(Collectors.toList());
    }
    
    public void deleteChat(Long userId, Long friendId) {
        validateFriendRelationship(userId, friendId);
        
        List<Message> messages = messageRepository.findChatHistory(userId, friendId);
        chatValidator.validateChatHistoryExists(messages);
        
        messages.forEach(message -> {
            if (message.getSenderId().equals(userId)) {
                message.deleteBySender();
            } else {
                message.deleteByReceiver();
            }
        });
    }
    
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void deleteOldMessages() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        messageRepository.deleteOldMessages(thirtyDaysAgo);
    }
} 