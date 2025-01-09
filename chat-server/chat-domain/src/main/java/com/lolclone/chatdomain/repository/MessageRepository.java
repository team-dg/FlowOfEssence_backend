package com.lolclone.chatdomain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lolclone.chatdomain.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
    /**
     * 특정 채팅방의 특정 날짜 범위 내 메시지 조회
     */
    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :roomId " +
            "AND m.createdAt BETWEEN :startDateTime AND :endDateTime " +
            "ORDER BY m.createdAt ASC")
    List<Message> findByRoomIdAndCreatedAtBetween(
        @Param("roomId") UUID roomId,
        @Param("startDateTime") LocalDateTime startDateTime,
        @Param("endDateTime") LocalDateTime endDateTime
    );

    /**
     * 특정 날짜 이전의 메시지 조회
     */
    @Query("SELECT m FROM Message m WHERE m.createdAt < :dateTime")
    List<Message> findByCreatedAtBefore(@Param("dateTime") LocalDateTime dateTime);

    Optional<Message> findTopByRoomIdOrderByCreatedAtDesc(UUID roomId);

    int deleteByRoomId(UUID roomId);

    long countByRoomId(UUID roomId);
}
