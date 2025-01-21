package com.lolclone.chatinfra.service.domain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.chatparticipant.ChatParticipant;
import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
import com.lolclone.chatdomain.domain.chatroom.ChatRoomId;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.domain.member.MemberId;
import com.lolclone.chatdomain.domain.message.Message;
import com.lolclone.chatdomain.exception.ParticipantNotFoundException;
import com.lolclone.chatdomain.repository.ChatParticipantRepository;
import com.lolclone.chatdomain.repository.ChatRoomRepository;
import com.lolclone.chatdomain.repository.MemberRepository;
import com.lolclone.chatdomain.repository.MessageRepository;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChatParticipantService {
    private final ChatParticipantRepository participantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    
    // /**
    //  * 채팅방 참여자 추가
    //  */
    // public ChatParticipant addParticipant(ChatRoomId roomId, MemberId userId) {
    //     ChatRoom chatRoom = chatRoomRepository.findById(roomId)
    //             .orElseThrow(() -> new NotFoundException(ExceptionType.CHAT_ROOM_NOT_FOUND));

    //     Member member = memberRepository.findById(userId)
    //             .orElseThrow(() -> new NotFoundException(ExceptionType.MEMBER_NOT_FOUND));

    //     // 도메인 모델에서 참여자 생성 및 유효성 검증 수행
    //     ChatParticipant participant = ChatParticipant.join(chatRoom, member);

    //     ChatParticipant savedParticipant = participantRepository.save(participant);
    //     //eventPublisher.publish(new ParticipantJoinedEvent(roomId, userId));

    //     return savedParticipant;
    // }

    // /**
    //  * 메시지 읽음 처리
    //  */
    // public void markMessageAsRead(ChatRoomId roomId, MemberId userId, MessageId messageId) {
    //     ChatParticipant participant = findParticipantOrThrow(roomId, userId);
    //     Message message = findMessageOrThrow(messageId);

    //     participant.readMessage(message);
    //     participantRepository.save(participant);

    //     //eventPublisher.publish(new MessageReadEvent(roomId, userId, messageId));
    // }

    // /**
    //  * 참여자 음소거 처리
    //  */
    // public void muteParticipant(ChatRoomId roomId, MemberId userId) {
    //     ChatParticipant participant = findParticipantOrThrow(roomId, userId);

    //     participant.mute();
    //     participantRepository.save(participant);

    //     //eventPublisher.publish(new ParticipantMutedEvent(roomId, userId));
    // }

    // /**
    //  * 참여자 음소거 해제
    //  */
    // public void unmuteParticipant(ChatRoomId roomId, MemberId userId) {
    //     ChatParticipant participant = findParticipantOrThrow(roomId, userId);

    //     participant.unmute();
    //     participantRepository.save(participant);

    //     //eventPublisher.publish(new ParticipantUnmutedEvent(roomId, userId));
    // }

    // /**
    //  * 채팅방 나가기
    //  */
    // public void leaveRoom(ChatRoomId roomId, MemberId userId) {
    //     ChatParticipant participant = findParticipantOrThrow(roomId, userId);

    //     participant.leave();
    //     participantRepository.save(participant);

    //     //eventPublisher.publish(new ParticipantLeftEvent(roomId, userId));
    // }

    // /**
    //  * 읽지 않은 메시지 수 조회
    //  */
    // @Transactional(readOnly = true)
    // public int getUnreadMessageCount(ChatRoomId roomId, MemberId userId) {
    //     ChatParticipant participant = findParticipantOrThrow(roomId, userId);
    //     List<Message> messages = messageRepository.findByChatRoomId(roomId);

    //     return participant.calculateUnreadCount(messages);
    // }

    // // 헬퍼 메서드
    // @Transactional(readOnly = true)
    // private ChatParticipant findParticipantOrThrow(ChatRoomId roomId, MemberId userId) {
    //     return participantRepository.findByChatRoomIdAndUserId(roomId, userId)
    //         .orElseThrow(() -> new ParticipantNotFoundException(userId, roomId));
    // }

    // @Transactional(readOnly = true)
    // private Message findMessageOrThrow(MessageId messageId) {
    //     return messageRepository.findById(messageId)
    //         .orElseThrow(() -> new NotFoundException(ExceptionType.MESSAGE_NOT_FOUND));
    // }
}
