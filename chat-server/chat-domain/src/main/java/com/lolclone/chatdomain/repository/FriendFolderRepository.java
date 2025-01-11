package com.lolclone.chatdomain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.FriendFolder;
import com.lolclone.chatdomain.domain.Member;

public interface FriendFolderRepository extends JpaRepository<FriendFolder, UUID> {
    List<FriendFolder> findByUser(Member user);
}
