package com.lolclone.chatdomain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lolclone.chatdomain.domain.chatroom.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID>{
    // @Query("SELECT cr FROM ChatRoom cr " +
    //        "JOIN cr.participants p1 " +
    //        "JOIN cr.participants p2 " +
    //        "WHERE cr.type = 'PERSONAL' " +
    //        "AND p1.user.id = :user1Id " +
    //        "AND p2.user.id = :user2Id")
    // Optional<ChatRoom> findPersonalRoomByParticipants(
    //     @Param("user1Id") UUID user1Id, 
    //     @Param("user2Id") UUID user2Id
    // );
}
