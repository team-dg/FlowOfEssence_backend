package com.lolclone.chatinfra.service.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.ChatParticipant;
import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.Friend;
import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.domain.Message;
import com.lolclone.chatdomain.domain.MessageType;
import com.lolclone.chatinfra.exception.commonexception.BadRequestException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;
import com.lolclone.chatinfra.service.domain.ChatParticipantService;
import com.lolclone.chatinfra.service.domain.ChatRoomService;
import com.lolclone.chatinfra.service.domain.FriendService;
import com.lolclone.chatinfra.service.domain.MemberService;
import com.lolclone.chatinfra.service.domain.MessageService;
import com.lolclone.chatserviceapi.dto.FriendChatInfoDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomService chatRoomService;
    private final ChatParticipantService chatParticipantService;
    private final MessageService messageService;
    private final MemberService memberService;
    private final FriendService friendService;

    /**
     * 친구와의 1대1 채팅방 생성 또는 조회
     * @param userId 사용자 ID
     * @param friendId 친구 ID
     * @return 생성되거나 조회된 채팅방
     */
    @Transactional
    public ChatRoom getOrCreatePersonalChatRoom(final UUID userId, final UUID friendId) {
        // 사용자와 친구 정보 조회 -> 없으면 예외 발생
        Map<UUID, Member> members = memberService.getMembersByIds(Arrays.asList(userId, friendId));
        Member user = members.get(userId);
        Member friend = members.get(friendId);
        
        // 친구 관계 확인 -> 친구가 아니면 예외 발생
        friendService.validateFriendship(user, friend);
        
        // 기존 1대1 채팅방 참여하고 있는게 있는지 조회
        return chatRoomService.getPersonalRoom(user, friend);
    }

    /**
     * 날짜별 채팅 메시지 조회
     * @param roomId 채팅방 ID
     * @param date 조회할 날짜
     * @return 해당 날짜의 메시지 목록
     */
    public List<Message> getChatMessagesByDate(final UUID roomId, final LocalDate date) {
        return messageService.getMessagesByRoomAndDate(roomId, date);
    }

    /**
     * 일정 기간이 지난 메시지 삭제
     * @param days 보관 기간 (일)
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void deleteOldMessages(final int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        
        // 삭제할 메시지 조회
        List<Message> oldMessages = messageService.getMessagesBeforeDate(cutoffDate);
        
        // 메시지 삭제
        for (Message message : oldMessages) {
            messageService.deleteMessage(message.getId());
            log.debug("메시지 삭제: messageId={}", message.getId());
        }
        
        log.info("오래된 메시지 삭제 완료: 삭제된 메시지 수={}", oldMessages.size());
    }

    /**
     * 친구와의 1대1 채팅 메시지 전송
     * @param roomId 채팅방 ID
     * @param senderId 발신자 ID
     * @param content 메시지 내용
     * @return 전송된 메시지
     */
    @Transactional
    public Message sendPersonalChatMessage(final UUID roomId, final UUID senderId, final String content) {
        log.info("1대1 채팅 메시지 전송 요청: roomId={}, senderId={}", roomId, senderId);
        
        // 채팅방 조회 및 개인 채팅방 여부 확인
        ChatRoom chatRoom = chatRoomService.getOrThrow(roomId);
        if (!chatRoom.isPersonal()) {
            throw new BadRequestException(ExceptionType.NOT_PERSONAL_CHAT_ROOM);
        }
        
        // 발신자 조회 및 채팅방 참여자 확인
        Member sender = memberService.getOrThrow(senderId);
        if (!chatParticipantService.isParticipant(chatRoom, sender)) {
            throw new BadRequestException(ExceptionType.NOT_CHAT_PARTICIPANT);
        }

        // 수신자 조회 (채팅방의 다른 참여자)
        Member recipient = chatParticipantService.getOtherParticipant(chatRoom, sender)
        .orElseThrow(() -> new BadRequestException(ExceptionType.RECIPIENT_NOT_FOUND));
        
        // 메시지 전송
        Message message = messageService.createMessage(chatRoom, sender, recipient, content, MessageType.TEXT);
        chatRoomService.updateLastMessage(roomId, message);
        
        log.info("1대1 채팅 메시지 전송 완료: messageId={}", message.getId());
        return message;
    }

    /**
     * 1대1 채팅방의 모든 대화 기록 삭제
     * @param roomId 채팅방 ID
     * @param userId 삭제 요청 사용자 ID
     */
    @Transactional
    public void deleteAllPersonalChatMessages(final UUID roomId, final UUID userId) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomService.getOrThrow(roomId);
        
        // 1대1 채팅방 여부 확인
        if (!chatRoom.isPersonal()) {
        throw new BadRequestException(ExceptionType.NOT_PERSONAL_CHAT_ROOM);
        }
        
        // 사용자 조회 및 채팅방 참여자 확인
        Member user = memberService.getOrThrow(userId);
        if (!chatParticipantService.isParticipant(chatRoom, user)) {
            throw new BadRequestException(ExceptionType.NOT_CHAT_PARTICIPANT);
        }
        
        // 모든 메시지 삭제
        messageService.deleteAllMessages(roomId);
        
        // 채팅방의 마지막 메시지 초기화
        chatRoomService.clearLastMessage(roomId);
    }

    /**
     * 사용자의 친구 목록을 가나다순으로 정렬하여 조회
     * 친구를 선택하면 바로 1대1 채팅이 가능합니다.
     * @param userId 사용자 ID
     * @return 정렬된 친구 목록과 해당 친구와의 채팅방 정보
     */
    public List<FriendChatInfoDto> getFriendListSortedByNickname(final UUID userId) {
        log.info("친구 목록 가나다순 정렬 요청: userId={}", userId);
        
        Member user = memberService.getOrThrow(userId);
        List<Friend> sortedFriends = friendService.getFriendsSortedByNickname(user);
        
        // 친구 목록을 FriendChatInfo로 변환 (채팅방 정보 포함)
        List<FriendChatInfoDto> friendChatInfos = sortedFriends.stream()
                .map(friend -> {
                    ChatRoom chatRoom = chatRoomService.getPersonalRoom(user, friend.getFriend());
                    ChatParticipant participant = chatParticipantService
                        .getParticipant(chatRoom, user)
                        .orElse(null);
                    int unreadMessageCount = participant != null ? chatParticipantService.getUnreadMessageCount(participant) : 0;
                    return FriendChatInfoDto.of(friend, chatRoom, participant, unreadMessageCount);
                })
        .collect(Collectors.toList());
        
        log.info("친구 목록 가나다순 정렬 완료: userId={}, friendCount={}", userId, friendChatInfos.size());
        return friendChatInfos;
    }
}
