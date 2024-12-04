package com.lolclone.chat_server.domain;

import com.lolclone.chat_server.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "messages")
public class Message extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long senderId;
    
    @Column(nullable = false)
    private Long receiverId;
    
    @Column(nullable = false, length = 500)
    private String message;
    
    @Column(nullable = false)
    private boolean deletedBySender;
    
    @Column(nullable = false)
    private boolean deletedByReceiver;
    
    private Message(Long senderId, Long receiverId, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.deletedBySender = false;
        this.deletedByReceiver = false;
    }
    
    public static Message of(Long senderId, Long receiverId, String message) {
        return new Message(senderId, receiverId, message);
    }
    
    public void deleteBySender() {
        this.deletedBySender = true;
    }
    
    public void deleteByReceiver() {
        this.deletedByReceiver = true;
    }
} 