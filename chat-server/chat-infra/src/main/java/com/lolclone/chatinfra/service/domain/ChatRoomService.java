package com.lolclone.chatinfra.service.domain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.exception.UnauthorizedParticipantException;
import com.lolclone.chatdomain.repository.ChatRoomRepository;
import com.lolclone.chatdomain.repository.MemberRepository;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatRoomService {
    // private final ChatRoomRepository chatRoomRepository;
    // private final MemberRepository memberRepository;

    // /**
    //  * 1:1 채팅방 생성 또는 조회
    //  * 이미 존재하는 1:1 채팅방이 있다면 해당 채팅방을 반환하고,
    //  * 없다면 새로운 채팅방을 생성합니다.
    //  */
    // public ChatRoom getOrCreatePersonalRoom(MemberId userId, MemberId targetId) {
    //     Member user = findMemberOrThrow(userId);
    //     Member target = findMemberOrThrow(targetId);

    //     return chatRoomRepository.findPersonalRoomByParticipants(userId, targetId)
    //             .orElseGet(() -> {
    //                 ChatRoom newRoom = ChatRoom.createPersonalRoom(user, target);
    //                 ChatRoom savedRoom = chatRoomRepository.save(newRoom);
    //                 //eventPublisher.publish(new PersonalChatRoomCreatedEvent(savedRoom.getId(), userId, targetId));
    //                 return savedRoom;
    //             });
    // }

    // /**
    //  * 그룹 채팅방 생성
    //  */
    // public ChatRoom createGroupRoom(MemberId creatorId, String name, List<MemberId> participantIds) {
    //     Member creator = findMemberOrThrow(creatorId);
    //     List<Member> participants = findParticipants(participantIds);

    //     ChatRoom groupRoom = ChatRoom.createGroupRoom(creator, name);
    //     participants.forEach(groupRoom::addParticipant);

    //     ChatRoom savedRoom = chatRoomRepository.save(groupRoom);
    //     //eventPublisher.publish(new GroupChatRoomCreatedEvent(savedRoom.getId(), creatorId, participantIds));

    //     return savedRoom;
    // }

    // /**
    //  * 채팅방 참여자 추가
    //  */
    // public void addParticipant(ChatRoomId roomId, MemberId userId, MemberId targetId) {
    //     ChatRoom chatRoom = findChatRoomOrThrow(roomId);
    //     Member user = findMemberOrThrow(userId);
    //     Member target = findMemberOrThrow(targetId);

    //     validateRoomParticipant(chatRoom, user);
    //     chatRoom.addParticipant(target);

    //     chatRoomRepository.save(chatRoom);
    //     //eventPublisher.publish(new ParticipantAddedEvent(roomId, targetId, userId));
    // }

    // /**
    //  * 채팅방 나가기
    //  */
    // public void leaveRoom(ChatRoomId roomId, MemberId userId) {
    //     ChatRoom chatRoom = findChatRoomOrThrow(roomId);
    //     Member user = findMemberOrThrow(userId);

    //     chatRoom.removeParticipant(user);

    //     chatRoomRepository.save(chatRoom);
    //     //eventPublisher.publish(new ParticipantLeftEvent(roomId, userId));
    // }

    // /**
    //  * 채팅방 메시지 전체 삭제
    //  */
    // public void clearMessages(ChatRoomId roomId, MemberId userId) {
    //     ChatRoom chatRoom = findChatRoomOrThrow(roomId);
    //     Member user = findMemberOrThrow(userId);

    //     chatRoom.clearMessages(user);

    //     chatRoomRepository.save(chatRoom);
    //     //eventPublisher.publish(new ChatRoomClearedEvent(roomId, userId));
    // }

    // private ChatRoom findChatRoomOrThrow(ChatRoomId roomId) {
    //     return chatRoomRepository.findById(roomId)
    //             .orElseThrow(() -> new NotFoundException(ExceptionType.CHAT_ROOM_NOT_FOUND));
    // }

    // private Member findMemberOrThrow(MemberId memberId) {
    //     return memberRepository.findById(memberId)
    //         .orElseThrow(() -> new NotFoundException(ExceptionType.MEMBER_NOT_FOUND));
    // }

    // private List<Member> findParticipants(List<MemberId> participantIds) {
    //     return memberRepository.findAllById(participantIds);
    // }

    // private void validateRoomParticipant(ChatRoom chatRoom, Member user) {
    //     if (!chatRoom.hasParticipant(user)) {
    //     throw new UnauthorizedParticipantException(user.getId(), chatRoom.getId());
    //     }
    // }
}
