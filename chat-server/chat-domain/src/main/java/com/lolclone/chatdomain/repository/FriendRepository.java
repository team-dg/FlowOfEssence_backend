package com.lolclone.chatdomain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.Friend;

public interface FriendRepository extends JpaRepository<Friend, UUID>{
    
}
