package com.lolclone.chatdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.FriendRequest;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    
}
