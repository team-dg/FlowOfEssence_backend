package com.lolclone.chatdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
    
}
