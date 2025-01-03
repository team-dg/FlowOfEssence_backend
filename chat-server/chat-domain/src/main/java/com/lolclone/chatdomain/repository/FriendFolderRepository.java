package com.lolclone.chatdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.FriendFolder;

public interface FriendFolderRepository extends JpaRepository<FriendFolder, Long> {
    
}
