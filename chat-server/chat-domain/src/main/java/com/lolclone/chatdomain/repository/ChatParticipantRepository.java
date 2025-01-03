package com.lolclone.chatdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.ChatParticipant;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long>{
    
}
