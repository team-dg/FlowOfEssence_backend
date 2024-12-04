package com.lolclone.chat_server.repository;

import com.lolclone.chat_server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    Optional<User> findById(Long id);
    
    @Query("SELECT u.nickname FROM User u WHERE u.id = :id")
    Optional<String> findUsernameById(@Param("id") Long id);
    
    boolean existsByNickname(String nickname);
    
    Optional<User> findByNickname(String nickname);
    
    @Query("SELECT u FROM User u WHERE u.nickname = :nickname AND u.tag = :tag")
    Optional<User> findByNicknameAndTag(@Param("nickname") String nickname, @Param("tag") String tag);
    
    @Query("SELECT u FROM User u WHERE " +
        "(LOWER(u.nickname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(u.tag) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
        "u.id IN (SELECT f.friend.id FROM Friend f WHERE f.user.id = :userId)")
    List<User> searchFriendsByQuery(@Param("userId") Long userId, @Param("query") String query);
    
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.nickname) LIKE LOWER(CONCAT('%', :nickname, '%')) " +
           "AND (:tag IS NULL OR LOWER(u.tag) LIKE LOWER(CONCAT('%', :tag, '%')))")
    List<User> findByNicknameContainingAndTagContaining(
        @Param("nickname") String nickname,
        @Param("tag") String tag
    );

    @Query(value = """
            SELECT u.* FROM users u
            INNER JOIN friends f ON f.friend_id = u.id
            WHERE f.user_id = :userId
            AND (
                LOWER(u.nickname) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(u.tag) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            ORDER BY
                CASE
                    WHEN LOWER(u.nickname) = LOWER(:query) THEN 0
                    WHEN LOWER(u.nickname) LIKE LOWER(CONCAT(:query, '%')) THEN 1
                    WHEN LOWER(u.tag) = LOWER(:query) THEN 2
                    ELSE 3
                END,
                u.status = 'ONLINE' DESC,
                u.status = 'IN_GAME' DESC,
                LENGTH(u.nickname),
                u.nickname
            LIMIT :size""", nativeQuery = true)
    List<User> searchFriendsForMessageWindow(
            @Param("userId") Long userId,
            @Param("query") String query,
            @Param("size") int size);
} 
