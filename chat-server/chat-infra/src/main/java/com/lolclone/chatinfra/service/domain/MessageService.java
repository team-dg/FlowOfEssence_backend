package com.lolclone.chatinfra.service.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.domain.Message;
import com.lolclone.chatdomain.domain.MessageType;
import com.lolclone.chatdomain.repository.MessageRepository;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;
import com.lolclone.chatinfra.handler.MessageHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageHandler messageHandler;

    public Message getOrThrow(final UUID id) {
        return messageRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.MESSAGE_NOT_FOUND));
    }

    /**
     * 특정 메시지를 특정 사용자가 읽었는지 확인합니다.
     * @param messageId 확인할 메시지 ID
     * @param user 확인할 사용자
     * @return 사용자가 메시지를 읽었으면 true, 아니면 false
     */
    public boolean isMessageReadBy(final UUID messageId, final Member user) {
        Message message = getOrThrow(messageId);
        return message.isReadBy(user);
    }

    /**
     * 특정 메시지가 시스템 메시지인지 확인합니다.
     * @param messageId 확인할 메시지 ID
     * @return 메시지가 시스템 메시지이면 true, 아니면 false
     */
    public boolean isSystemMessage(final UUID messageId) {
        Message message = getOrThrow(messageId);
        return message.isSystemMessage();
    }

    /**
     * 특정 메시지가 귓속말 메시지인지 확인합니다.
     * @param messageId 확인할 메시지 ID
     * @return 메시지가 귓속말 메시지이면 true, 아니면 false
     */
    public boolean isWhisperMessage(final UUID messageId) {
        Message message = getOrThrow(messageId);
        return message.isWhisper();
    }

    /**
     * 특정 메시지의 수신자가 특정 사용자인지 확인합니다.
     * @param messageId 확인할 메시지 ID
     * @param user 확인할 사용자
     * @return 사용자가 메시지의 수신자이면 true, 아니면 false
     */
    public boolean isRecipient(final UUID messageId, final Member user) {
        Message message = getOrThrow(messageId);
        return message.isRecipient(user);
    }

    /**
     * 특정 사용자가 특정 메시지에 접근할 수 있는지 확인
     * 귓속말 메시지의 경우, 발신자 또는 수신자만 접근 가능
     * @param messageId 확인할 메시지 ID
     * @param user 확인할 사용자
     * @return 사용자가 메시지에 접근할 수 있으면 true, 아니면 false
     */
    public boolean canAccessMessage(final UUID messageId, final Member user) {
        Message message = getOrThrow(messageId);
        if (message.isWhisper()) {
            return message.getSender().equals(user) || message.isRecipient(user);
        }
        return true;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Message createMessage(final ChatRoom chatRoom, final Member sender, final Member recipient, final String content, final MessageType type) {
        // 1. 타입에 따른 메시지 생성
        Message message = switch (type) {
            case TEXT -> Message.createTextMessage(chatRoom, sender, recipient, content);
            case SYSTEM -> Message.createSystemMessage(chatRoom, content);
            case WHISPER -> Message.createWhisperMessage(chatRoom, sender, recipient, content);
        };
        // 2. 메시지 저장
        Message savedMessage = messageRepository.save(message);
        
        // 2. 메시지 전송 이벤트 발행
        messageHandler.sendMessage(savedMessage);
        return savedMessage;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void markMessageAsRead(final UUID messageId, final Member user) {
        Message message = getOrThrow(messageId);
        message.markAsReadBy(user);
        // 메시지 읽음 처리 이벤트 발행 (필요한 경우)
    }

    /**
     * 특정 채팅방의 특정 날짜 메시지 조회
     * @param roomId 채팅방 ID
     * @param date 조회할 날짜
     * @return 해당 날짜의 메시지 목록
     */
    public List<Message> getMessagesByRoomAndDate(final UUID roomId, final LocalDate date) {
        log.info("날짜별 채팅 메시지 조회: roomId={}, date={}", roomId, date);
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        List<Message> messages = messageRepository.findByRoomIdAndCreatedDateBetween(roomId, startOfDay, endOfDay);
        
        log.info("날짜별 채팅 메시지 조회 완료: roomId={}, date={}, messageCount={}", roomId, date, messages.size());
        
        return messages;
    }

    /**
     * 특정 날짜 이전의 메시지 조회
     * @param dateTime 기준 날짜시간
     * @return 기준 날짜시간 이전의 메시지 목록
     */
    public List<Message> getMessagesBeforeDate(final LocalDateTime dateTime) {
        log.info("특정 날짜 이전 메시지 조회: dateTime={}", dateTime);
        
        List<Message> messages = messageRepository.findByCreatedDateBefore(dateTime);
        
        log.info("특정 날짜 이전 메시지 조회 완료: dateTime={}, messageCount={}", 
        dateTime, messages.size());
        
        return messages;
    }

    /**
     * 메시지 삭제
     * @param messageId 삭제할 메시지 ID
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteMessage(final UUID messageId) {
        log.info("메시지 삭제: messageId={}", messageId);
        messageRepository.deleteById(messageId);
        log.info("메시지 삭제 완료: messageId={}", messageId);
    }

    /**
     * 채팅방의 마지막 메시지 조회
     */
    public Message getLastMessage(final UUID roomId) {
        return messageRepository.findTopByChatRoomIdOrderByCreatedDateDesc(roomId).orElse(null);
    }

    /**
     * 채팅방의 모든 메시지 삭제
     * @return 삭제된 메시지 수
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public int deleteAllMessages(final UUID roomId) {
        return messageRepository.deleteByChatRoomId(roomId);
    }

    /**
     * 채팅방의 전체 메시지 수 조회
     */
    public long getMessageCount(UUID roomId) {
        return messageRepository.countByChatRoomId(roomId);
    }
}
