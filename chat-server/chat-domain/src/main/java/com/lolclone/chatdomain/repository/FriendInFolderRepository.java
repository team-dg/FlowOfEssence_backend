package com.lolclone.chatdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.FriendInFolder;

public interface FriendInFolderRepository extends JpaRepository<FriendInFolder, Long> {
    
}
