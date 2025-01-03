package com.lolclone.chat_server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "friend_request_id", columnDefinition = "uuid")
    private UUID id;
    
    @Column(name = "sender_id", nullable = false)
    private UUID senderId;
    
    @Column(name = "receiver_id", nullable = false)
    private UUID receiverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", insertable = false, updatable = false)
    private User receiver;

    private FriendRequest(UUID senderId, UUID receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public static FriendRequest of(UUID senderId, UUID receiverId) {
        return new FriendRequest(senderId, receiverId);
    }
} 