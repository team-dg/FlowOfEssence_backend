package com.lolclone.chat_server.repository;

import com.lolclone.chat_server.domain.FriendFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendFolderRepository extends JpaRepository<FriendFolder, Long> {
    
    @Query("SELECT f FROM FriendFolder f WHERE f.user.id = :userId")
    List<FriendFolder> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT f FROM FriendFolder f LEFT JOIN FETCH f.friends WHERE f.id = :folderId")
    Optional<FriendFolder> findByIdWithFriends(@Param("folderId") Long folderId);
    
    @Query("SELECT COUNT(f) > 0 FROM FriendFolder f WHERE f.user.id = :userId AND f.name = :name")
    boolean existsByUser_IdAndName(@Param("userId") Long userId, @Param("name") String name);
}
