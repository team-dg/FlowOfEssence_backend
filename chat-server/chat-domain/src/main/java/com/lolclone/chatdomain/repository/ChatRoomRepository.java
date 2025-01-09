package com.lolclone.chatdomain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.Member;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID>{
    Optional<ChatRoom> findById(UUID id);
    
    /**
     * 두 사용자가 참여중인 1대1 채팅방 조회
     */
    @Query("SELECT cr FROM ChatRoom cr " +
           "JOIN cr.participants p1 " +
           "JOIN cr.participants p2 " +
           "WHERE cr.type = 'PERSONAL' " +
           "AND p1.user = :user1 " +
           "AND p2.user = :user2")
    Optional<ChatRoom> findPersonalRoomByParticipants(Member user1, Member user2);
}
