package com.lolclone.chatdomain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.FriendRequest;
import com.lolclone.chatdomain.domain.FriendStatus;
import com.lolclone.chatdomain.domain.Member;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverAndStatus(Member receiver, FriendStatus status);
    List<FriendRequest> findByRequesterAndStatus(Member requester, FriendStatus status);
    Optional<FriendRequest> findByRequesterAndReceiverAndStatus(Member requester, Member receiver, FriendStatus status);
}

