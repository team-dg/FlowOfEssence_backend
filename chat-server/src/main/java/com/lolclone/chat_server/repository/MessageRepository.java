package com.lolclone.chat_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lolclone.chat_server.domain.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    
    @Query("""
        SELECT m FROM Message m 
        WHERE ((m.senderId = :userId AND m.receiverId = :friendId AND m.deletedBySender = false)
        OR (m.senderId = :friendId AND m.receiverId = :userId AND m.deletedByReceiver = false))
        ORDER BY m.createdDate DESC
        """)
    List<Message> findChatHistory(
        @Param("userId") UUID userId,
        @Param("friendId") UUID friendId
    );
    
    @Query("""
        DELETE FROM Message m 
        WHERE m.createdDate < :date 
        AND m.deletedBySender = true 
        AND m.deletedByReceiver = true
        """)
    void deleteOldMessages(@Param("date") LocalDateTime date);
} 