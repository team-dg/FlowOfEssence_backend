package com.lolclone.chat_server.repository;

import com.lolclone.chat_server.domain.FriendFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendFolderRepository extends JpaRepository<FriendFolder, UUID> {
    
    @Query("SELECT f FROM FriendFolder f WHERE f.user.id = :userId")
    List<FriendFolder> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT f FROM FriendFolder f LEFT JOIN FETCH f.friends WHERE f.id = :folderId")
    Optional<FriendFolder> findByIdWithFriends(@Param("folderId") UUID folderId);
    
    @Query("SELECT COUNT(f) > 0 FROM FriendFolder f WHERE f.user.id = :userId AND f.name = :name")
    boolean existsByUser_IdAndName(@Param("userId") UUID userId, @Param("name") String name);
}
