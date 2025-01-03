package com.lolclone.chatdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
    
}
