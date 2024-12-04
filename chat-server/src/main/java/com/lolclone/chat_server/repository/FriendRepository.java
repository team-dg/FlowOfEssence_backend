package com.lolclone.chat_server.repository;

import com.lolclone.chat_server.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT f FROM Friend f WHERE f.user.id = :userId")
    List<Friend> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT f FROM Friend f WHERE f.user.id = :userId ORDER BY f.friend.nickname ASC")
    List<Friend> findByUserIdOrderByFriendNameAsc(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(f) > 0 FROM Friend f WHERE f.user.id = :userId AND f.friend.id = :friendId")
    boolean existsByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);
    
    @Query("SELECT f FROM Friend f WHERE f.user.id = :userId AND f.friend.id = :friendId")
    Optional<Friend> findByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);
} 