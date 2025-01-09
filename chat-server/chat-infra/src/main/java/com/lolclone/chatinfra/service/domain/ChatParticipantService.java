package com.lolclone.chatinfra.service.domain;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.ChatParticipant;
import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.repository.ChatParticipantRepository;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatParticipantService {
    private final MessageService messageService;
    private final ChatParticipantRepository chatParticipantRepository;

    public ChatParticipant getOrThrow(final Long id) {
        return chatParticipantRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.CHAT_PARTICIPANT_NOT_FOUND));
    }

    /**
     * 읽지 않은 메시지가 있는지 확인
     */
    public boolean hasUnreadMessages(final Long participantId, final Long latestMessageId) {
        ChatParticipant participant = getOrThrow(participantId);
        return participant.hasUnreadMessages(latestMessageId);
    }

    /**
     * 음소거 상태인지 확인
     */
    public boolean isMuted(final Long participantId) {
        ChatParticipant participant = getOrThrow(participantId);
        return participant.isMuted();
    }

    /**
     * 특정 사용자가 특정 채팅방의 참여자인지 확인
     */
    public boolean isParticipant(final ChatRoom chatRoom, final Member user) {
        return getParticipant(chatRoom, user).isPresent();
    }

    /**
     * 채팅방 참여자 생성
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public ChatParticipant createChatParticipant(final ChatRoom chatRoom, final Member user) {
        ChatParticipant participant = ChatParticipant.of(chatRoom, user);
        return chatParticipantRepository.save(participant);
    }

    /**
     * 메시지 읽음 처리
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateLastReadMessage(final Long participantId, final Long messageId) {
        ChatParticipant participant = getOrThrow(participantId);
        participant.updateLastReadMessage(messageId);
    }

    /**
     * 채팅방 참여자 음소거 처리
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void muteChatParticipant(final Long participantId) {
        ChatParticipant participant = getOrThrow(participantId);
        participant.mute();
    }

    /**
     * 채팅방 참여자 음소거 해제
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void unmuteChatParticipant(final Long participantId) {
        ChatParticipant participant = getOrThrow(participantId);
        participant.unmute();
    }

    /**
     * 읽지 않은 메시지 수 조회
     * @param participant 채팅방 참여자
     * @return 읽지 않은 메시지 수
     */
    public int getUnreadMessageCount(ChatParticipant participant) {
        long totalMessageCount = messageService.getMessageCount(participant.getChatRoom().getId());
        return participant.getUnreadMessageCount(totalMessageCount);
    }

    /**
     * 채팅방과 사용자로 채팅 참여자 조회
     * @param chatRoom 채팅방
     * @param user 사용자
     * @return 채팅 참여자 (Optional)
     */
    public Optional<ChatParticipant> getParticipant(ChatRoom chatRoom, Member user) {
        log.debug("채팅 참여자 조회: roomId={}, userId={}", chatRoom.getId(), user.getId());
        return chatParticipantRepository.findByChatRoomAndUser(chatRoom, user);
    }

    /**
     * 채팅방에서 특정 사용자를 제외한 다른 참여자 조회 (1대1 채팅용)
     * @param chatRoom 채팅방
     * @param user 제외할 사용자
     * @return 다른 참여자
     */
    public Optional<Member> getOtherParticipant(ChatRoom chatRoom, Member user) {
        return chatParticipantRepository.findByChatRoom(chatRoom).stream()
            .map(ChatParticipant::getUser)
            .filter(participant -> !participant.getId().equals(user.getId()))
            .findFirst();
    }
}
