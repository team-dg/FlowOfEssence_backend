package com.lolclone.chatinfra.service.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
import com.lolclone.chatdomain.domain.chatroom.ChatRoomId;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.domain.member.MemberId;
import com.lolclone.chatdomain.domain.message.Message;
import com.lolclone.chatdomain.exception.UnauthorizedParticipantException;
import com.lolclone.chatdomain.repository.ChatRoomRepository;
import com.lolclone.chatdomain.repository.MemberRepository;
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
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final MessageHandler messageHandler;

    // /**
    //  * 메시지 전송
    //  */
    // public Message sendMessage(MessageId messageId, ChatRoomId roomId, MemberId senderId, String content) {
    //     ChatRoom chatRoom = findChatRoomById(roomId);
    //     Member sender = findMemberById(senderId);

    //     // 채팅방에서 메시지 생성 (도메인 로직)
    //     Message message = chatRoom.sendMessage(sender, content);
        
    //     // 저장
    //     Message savedMessage = messageRepository.save(message);
        
    //     // 이벤트 발행
    //     // eventPublisher.publish(new MessageSentEvent(roomId, messageId, senderId));
        
    //     return savedMessage;
    // }

    // /**
    //  * 메시지 읽음 처리
    //  */
    // public void markAsRead(MessageId messageId, MemberId readerId) {
    //     Message message = findMessageById(messageId);
    //     Member reader = findMemberById(readerId);

    //     message.markAsRead(reader);
    //     messageRepository.save(message);
    // }

    // /**
    //  * 날짜별 메시지 조회
    //  */
    // public List<Message> getMessagesByDate(ChatRoomId roomId, MemberId userId, LocalDate date) {
    //     ChatRoom chatRoom = findChatRoomById(roomId);
    //     Member user = findMemberById(userId);

    //     // 채팅방 참여자 검증
    //     if (!chatRoom.hasParticipant(user)) {
    //         throw new UnauthorizedParticipantException(userId, roomId);
    //     }

    //     LocalDateTime startOfDay = date.atStartOfDay();
    //     LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

    //     return messageRepository.findByChatRoomAndCreatedAtBetween(chatRoom, startOfDay, endOfDay);
    // }

    // /**
    //  * 특정 기간 이전의 메시지 삭제
    //  */
    // @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    // public void deleteOldMessages() {
    //     LocalDateTime threshold = LocalDateTime.now().minusDays(30); // 30일 이전 메시지
    //     messageRepository.deleteAllByCreatedAtBefore(threshold);
    // }

    // /**
    //  * 채팅방의 모든 메시지 삭제
    //  */
    // public void clearChatRoomMessages(ChatRoomId roomId, MemberId userId) {
    //     ChatRoom chatRoom = findChatRoomById(roomId);
    //     Member user = findMemberById(userId);

    //     chatRoom.clearMessages(user);
    //     chatRoomRepository.save(chatRoom);

    //     // eventPublisher.publish(new ChatRoomClearedEvent(roomId, userId));
    // }

    // /**
    //  * 읽지 않은 메시지 수 조회
    //  */
    // public long countUnreadMessages(ChatRoomId roomId, MemberId userId) {
    //     return messageRepository.countUnreadMessages(roomId, userId);
    // }

    // // @Transactional(propagation = Propagation.MANDATORY)
    // // public Message createMessage(final ChatRoom chatRoom, final Member sender, final Member recipient, final String content, final MessageType type) {
    // //     // 1. 타입에 따른 메시지 생성
    // //     Message message = switch (type) {
    // //         case TEXT -> Message.createTextMessage(chatRoom, sender, recipient, content);
    // //         case SYSTEM -> Message.createSystemMessage(chatRoom, content);
    // //         case WHISPER -> Message.createWhisperMessage(chatRoom, sender, recipient, content);
    // //     };
    // //     // 2. 메시지 저장
    // //     Message savedMessage = messageRepository.save(message);
        
    // //     // 2. 메시지 전송 이벤트 발행
    // //     messageHandler.sendMessage(savedMessage);
    // //     return savedMessage;
    // // }

    // //헬퍼 메소드
    // private ChatRoom findChatRoomById(final ChatRoomId roomId) {
    //     return chatRoomRepository.findById(roomId).orElseThrow(() -> new NotFoundException(ExceptionType.CHATROOM_NOT_FOUND));
    // }

    // private Member findMemberById(final MemberId memberId) {
    //     return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ExceptionType.MEMBER_NOT_FOUND));
    // }

    // private Message findMessageById(final MessageId messageId) {
    //     return messageRepository.findById(messageId).orElseThrow(() -> new NotFoundException(ExceptionType.MESSAGE_NOT_FOUND));
    // }
}
