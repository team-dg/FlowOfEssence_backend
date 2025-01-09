package com.lolclone.chatdomain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.Friend;
import com.lolclone.chatdomain.domain.Member;

public interface FriendRepository extends JpaRepository<Friend, UUID>{
    List<Friend> findByUser(Member user);
    List<Friend> findByUserAndIsBlockedTrue(Member user);
    Optional<Friend> findByUserAndFriend(Member user, Member friend);
    List<Friend> findByUserAndBlockedFalseAndBlockedByFalse(Member user);
    List<Friend> findByUserAndIsBlockedFalse(Member user);
}
