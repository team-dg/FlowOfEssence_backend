package com.lolclone.chatdomain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.ChatParticipant;
import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.Member;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long>{
    Optional<ChatParticipant> findByChatRoomAndUser(ChatRoom chatRoom, Member user);
    List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);
}
