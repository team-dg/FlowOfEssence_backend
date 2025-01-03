package com.lolclone.chat_server.repository;

import com.lolclone.chat_server.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, UUID> {
    @Query("SELECT f FROM Friend f WHERE f.user.id = :userId")
    List<Friend> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT f FROM Friend f WHERE f.user.id = :userId ORDER BY f.friend.nickname ASC")
    List<Friend> findByUserIdOrderByFriendNameAsc(@Param("userId") UUID userId);
    
    @Query("SELECT COUNT(f) > 0 FROM Friend f WHERE f.user.id = :userId AND f.friend.id = :friendId")
    boolean existsByUserIdAndFriendId(@Param("userId") UUID userId, @Param("friendId") UUID friendId);
    
    @Query("SELECT f FROM Friend f WHERE f.user.id = :userId AND f.friend.id = :friendId")
    Optional<Friend> findByUserIdAndFriendId(@Param("userId") UUID userId, @Param("friendId") UUID friendId);
} 