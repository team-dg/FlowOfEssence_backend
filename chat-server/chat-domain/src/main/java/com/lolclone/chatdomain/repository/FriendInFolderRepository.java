package com.lolclone.chatdomain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.Friend;
import com.lolclone.chatdomain.domain.FriendFolder;
import com.lolclone.chatdomain.domain.FriendInFolder;

public interface FriendInFolderRepository extends JpaRepository<FriendInFolder, UUID> {
    List<FriendInFolder> findByFolder(FriendFolder folder);
    List<FriendInFolder> findByFriend(Friend friend);
    Optional<FriendInFolder> findByFolderAndFriend(FriendFolder folder, Friend friend);
}
