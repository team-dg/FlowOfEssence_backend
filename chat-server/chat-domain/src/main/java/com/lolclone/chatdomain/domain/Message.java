package com.lolclone.chatdomain.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.lolclone.chatdomain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 메시지 정보 관리 (생성, 조회)
 * 메시지 전송 및 수신
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "messages")
public class Message extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false, name = "sender_id", columnDefinition = "uuid")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Member recipient;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;

    @ElementCollection
    @CollectionTable(
        name = "message_read_status",
        joinColumns = @JoinColumn(name = "message_id")
    )
    private Set<UUID> readByUsers = new HashSet<>();

    private Message(ChatRoom chatRoom, Member sender, Member recipient, String content, MessageType messageType) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.messageType = messageType;
        this.sentAt = LocalDateTime.now();
    }

    // 정적 팩토리 메서드
    public static Message createTextMessage(ChatRoom chatRoom, Member sender, Member recipient, String content) {
        return new Message(chatRoom, sender, recipient, content, MessageType.TEXT);
    }

    public static Message createSystemMessage(ChatRoom chatRoom, String content) {
        return new Message(chatRoom, null, null, content, MessageType.SYSTEM);
    }

    public static Message createWhisperMessage(ChatRoom chatRoom, Member sender, Member recipient, String content) {
        return new Message(chatRoom, sender, recipient, content, MessageType.WHISPER);
    }

    // 비즈니스 메서드
    /**
     * 특정 사용자가 메시지를 읽었음을 표시하는 메서드
     * @param user 메시지를 읽은 사용자
     */
    public void markAsReadBy(Member user) {
        this.readByUsers.add(user.getId());
    }

    /**
     * 특정 사용자가 메시지를 읽었는지 확인하는 메서드
     * @param user 확인할 사용자
     * @return 메시지를 읽었으면 true, 아니면 false
     */
    public boolean isReadBy(Member user) {
        return this.readByUsers.contains(user.getId());
    }

    /**
     * 시스템 메시지인지 확인하는 메서드
     * @return 시스템 메시지이면 true, 아니면 false
     */
    public boolean isSystemMessage() {
        return this.messageType == MessageType.SYSTEM;
    }

    /**
     * 귓속말 메시지인지 확인하는 메서드
     * @return 귓속말 메시지이면 true, 아니면 false
     */
    public boolean isWhisper() {
        return this.messageType == MessageType.WHISPER;
    }

    /**
     * 특정 사용자가 메시지의 수신자인지 확인하는 메서드
     * @param user 확인할 사용자
     * @return 메시지의 수신자이면 true, 아니면 false
     */
    public boolean isRecipient(Member user) {
        return this.recipient != null && this.recipient.equals(user);
    }
}
