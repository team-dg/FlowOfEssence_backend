package com.lolclone.chatdomain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.FriendFolder;
import com.lolclone.chatdomain.domain.Member;

public interface FriendFolderRepository extends JpaRepository<FriendFolder, Long> {
    List<FriendFolder> findByUser(Member user);
}
