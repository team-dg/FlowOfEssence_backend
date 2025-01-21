package com.lolclone.chatdomain.repository.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lolclone.chatdomain.domain.friend.Friend;
import com.lolclone.chatdomain.domain.friend.FriendId;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId>, QuerydslFriendRepository {

}
