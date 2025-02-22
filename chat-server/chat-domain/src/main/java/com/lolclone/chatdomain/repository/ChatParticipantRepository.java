package com.lolclone.chatdomain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.chatparticipant.ChatParticipant;
import com.lolclone.chatdomain.domain.chatparticipant.ChatParticipantId;
import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
import com.lolclone.chatdomain.domain.member.Member;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantId> {
    // Optional<ChatParticipant> findByChatRoomAndUser(ChatRoom chatRoom, Member user);
    // List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);
    // Optional<ChatParticipant> findByChatRoomIdAndUserId(UUID chatRoomId, UUID userId);
}
