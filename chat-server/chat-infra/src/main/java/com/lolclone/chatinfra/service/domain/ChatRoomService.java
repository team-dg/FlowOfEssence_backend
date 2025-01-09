package com.lolclone.chatinfra.service.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.domain.Message;
import com.lolclone.chatdomain.repository.ChatRoomRepository;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatParticipantService chatParticipantService;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom getOrThrow(final UUID id) {
        return chatRoomRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.CHAT_ROOM_NOT_FOUND));
    }

    /**
     * 개인 채팅방 생성
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public ChatRoom createPersonalRoom(final Member creator) {
        ChatRoom chatRoom = ChatRoom.createPersonalRoom(creator);
        return chatRoomRepository.save(chatRoom);
    }

    /**
     * 그룹 채팅방 생성
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public ChatRoom createGroupRoom(final Member creator) {
        ChatRoom chatRoom = ChatRoom.createGroupRoom(creator);
        return chatRoomRepository.save(chatRoom);
    }

    /**
     * 채팅방에 참여자 추가
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addParticipant(final UUID roomId, final Member member) {
        ChatRoom chatRoom = getOrThrow(roomId);
        chatRoom.addParticipant(member);
    }

    /**
     * 채팅방에서 참여자 제거
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeParticipant(final UUID roomId, final Member member) {
        ChatRoom chatRoom = getOrThrow(roomId);
        chatRoom.removeParticipant(member);
    }

    /**
     * 채팅방의 마지막 메시지 업데이트
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateLastMessage(final UUID roomId, final Message message) {
        ChatRoom chatRoom = getOrThrow(roomId);
        chatRoom.updateLastMessage(message);
    }

    /**
     * 채팅방 비활성화
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deactivateRoom(final UUID roomId) {
        ChatRoom chatRoom = getOrThrow(roomId);
        chatRoom.deactivate();
    }

    /**
     * 채팅방이 1대1 채팅방인지 확인
     * @param roomId 채팅방 ID
     * @return 1대1 채팅방이면 true, 아니면 false
     */
    public boolean isPersonalRoom(final UUID roomId) {
        ChatRoom chatRoom = getOrThrow(roomId);
        return chatRoom.isPersonal();
    }

    /**
     * 채팅방 타입 확인 (그룹)
     */
    public boolean isGroupRoom(final UUID roomId) {
        ChatRoom chatRoom = getOrThrow(roomId);
        return chatRoom.isGroup();
    }

    /**
     * 특정 사용자가 채팅방 참여자인지 확인
     */
    public boolean hasParticipant(final UUID roomId, final Member member) {
        ChatRoom chatRoom = getOrThrow(roomId);
        return chatRoom.hasParticipant(member);
    }

    /**
     * 두 사용자가 참여중인 1 대1 채팅방 조회
     * @param user1 조회할 사용자
     * @param user2 조회할 사용자
     * @return 사용자가 참여중인 채팅방 목록
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public ChatRoom getPersonalRoom(final Member user1, final Member user2) {
        return chatRoomRepository.findPersonalRoomByParticipants(user1, user2)
                .orElseGet(() -> {
                    ChatRoom newRoom = this.createPersonalRoom(user1);
                    chatParticipantService.createChatParticipant(newRoom, user2);
                    return newRoom;
                });
    }

    /**
     * 두 사용자 간의 기존 1:1 채팅방을 조회합니다.
     * 
     * @return 존재하는 채팅방 또는 Optional.empty()
     */
    public Optional<ChatRoom> findPersonalRoom(final Member user1, final Member user2) {
        return chatRoomRepository.findPersonalRoomByParticipants(user1, user2);
    }

    /**
     * 채팅방의 마지막 메시지 초기화
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void clearLastMessage(final UUID roomId) {
        ChatRoom chatRoom = getOrThrow(roomId);
        chatRoom.clearLastMessage();
    }
}
