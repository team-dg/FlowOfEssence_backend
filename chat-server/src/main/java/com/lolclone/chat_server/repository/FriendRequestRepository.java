package com.lolclone.chat_server.repository;

import com.lolclone.chat_server.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    
    /**
     * 특정 수신자의 친구 요청 목록을 조회합니다.
     */
    List<FriendRequest> findByReceiverId(Long receiverId);
    
    /**
     * 특정 수신자의 친구 요청 수를 조회합니다.
     */
    long countByReceiverId(Long receiverId);
    
    /**
     * 특정 발신자와 수신자 간의 친구 요청을 조회합니다.
     */
    Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    
    /**
     * 특정 발신자와 수신자 간의 친구 요청이 존재하는지 확인합니다.
     */
    boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId);
    
    /**
     * 특정 발신자와 수신자 간의 친구 요청을 삭제합니다.
     */
    void deleteBySenderIdAndReceiverId(Long senderId, Long receiverId);
} 