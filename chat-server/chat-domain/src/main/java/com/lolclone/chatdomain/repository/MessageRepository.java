package com.lolclone.chatdomain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
import com.lolclone.chatdomain.domain.chatroom.ChatRoomId;
import com.lolclone.chatdomain.domain.member.MemberId;
import com.lolclone.chatdomain.domain.message.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
    // /**
    //  * 특정 채팅방의 특정 날짜 범위 내 메시지 조회
    //  */
    // @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :roomId " +
    //         "AND m.createdDate BETWEEN :startDateTime AND :endDateTime " +
    //         "ORDER BY m.createdDate ASC")
    // List<Message> findByRoomIdAndCreatedDateBetween(
    //     @Param("roomId") UUID roomId,
    //     @Param("startDateTime") LocalDateTime startDateTime,
    //     @Param("endDateTime") LocalDateTime endDateTime
    // );

    // /**
    //  * 특정 날짜 이전의 메시지 조회
    //  */
    // @Query("SELECT m FROM Message m WHERE m.createdDate < :dateTime")
    // List<Message> findByCreatedDateBefore(@Param("dateTime") LocalDateTime dateTime);

    // Optional<Message> findTopByChatRoomIdOrderByCreatedDateDesc(UUID roomId);

    // int deleteByChatRoomId(UUID roomId);

    // long countByChatRoomId(UUID roomId);

    List<Message> findByChatRoomId(ChatRoomId chatRoomId);

    List<Message> findByChatRoomAndCreatedAtBetween(ChatRoom chatRoom, LocalDateTime startOfDay, LocalDateTime endOfDay);

    void deleteAllByCreatedAtBefore(LocalDateTime threshold);

    long countUnreadMessages(ChatRoomId roomId, MemberId userId);
}
