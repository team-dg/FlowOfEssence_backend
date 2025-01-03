package com.lolclone.chat_server.domain;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "message_id", columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false)
    private UUID senderId;
    
    @Column(nullable = false)
    private UUID receiverId;
    
    @Column(nullable = false, length = 500)
    private String message;
    
    @Column(nullable = false)
    private boolean deletedBySender;
    
    @Column(nullable = false)
    private boolean deletedByReceiver;
    
    private Message(UUID senderId, UUID receiverId, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.deletedBySender = false;
        this.deletedByReceiver = false;
    }
    
    public static Message of(UUID senderId, UUID receiverId, String message) {
        return new Message(senderId, receiverId, message);
    }
    
    public void deleteBySender() {
        this.deletedBySender = true;
    }
    
    public void deleteByReceiver() {
        this.deletedByReceiver = true;
    }
} 