package com.lolclone.chat_server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.lolclone.chat_server.common.domain.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "friend_requests",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_friend_request",
            columnNames = {"sender_id", "receiver_id"}
        )
    },
    indexes = {
        @Index(name = "idx_sender_receiver", columnList = "sender_id, receiver_id")
    }
)
public class FriendRequest extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sender_id", nullable = false)
    private Long senderId;
    
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", insertable = false, updatable = false)
    private User receiver;

    private FriendRequest(Long senderId, Long receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public static FriendRequest of(Long senderId, Long receiverId) {
        return new FriendRequest(senderId, receiverId);
    }
} 