package com.lolclone.chatdomain.domain.message;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
import com.lolclone.chatdomain.domain.chatroom.ChatRoom.TeamColor;
import com.lolclone.chatdomain.domain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.exception.MessageNotDeletableException;
import com.lolclone.chatdomain.exception.MessageNotEditableException;
import com.lolclone.chatdomain.exception.UnauthorizedMessageAccessException;
import com.lolclone.chatdomain.exception.UnauthorizedMessageModificationException;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Member recipient; // 귓속말 수신자 (WHISPER 타입 시 필수)

    @Embedded
    private MessageContent messageContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MessageType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_color")
    private TeamColor teamColor; // BLUE, RED

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt; // 메시지 만료 시간 (TTL)

    @Column(name = "edited")
    private boolean edited;

    @ElementCollection
    @CollectionTable(
        name = "message_read_status",
        joinColumns = @JoinColumn(name = "message_id")
    )
    private Map<UUID, LocalDateTime> readByUsers;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "game_session_id")
    private UUID gameSessionId; // 게임 세션 ID (Feature 16)

    @Builder
    private Message(ChatRoom chatRoom, Member sender, String content, MessageType type) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.messageContent = MessageContent.of(content);
        this.type = type;
        this.sentAt = LocalDateTime.now();
        this.edited = false;
        this.readByUsers = new HashMap<>();
        this.deleted = false;
    }

    // 정적 팩토리 메서드
    public static Message create(ChatRoom chatRoom, Member sender, String content, Duration ttl) {
        Message message = new Message(chatRoom, sender, content, MessageType.TEXT);
        message.expiresAt = LocalDateTime.now().plus(ttl);
        return message;
    }

    public static Message createSystem(ChatRoom chatRoom, String content) {
        return new Message(chatRoom, null, content, MessageType.SYSTEM);
    }

    //TODO : 추후 수정 상대방에 대한 정보 없음
    public static Message createWhisperMessage(ChatRoom chatRoom, Member sender, Member recipient, String content) {
        return new Message(chatRoom, sender, content, MessageType.WHISPER);
    }

    // 비즈니스 메서드
    public void markAsRead(Member reader) {
        validateParticipant(reader);
        this.readByUsers.put(reader.getId(), LocalDateTime.now());
    }

    public void edit(Member editor, String newContent) {
        validateSender(editor);
        validateEditable();
        this.messageContent = MessageContent.of(newContent);
        this.edited = true;
    }

    public void delete(Member deleter) {
        validateSender(deleter);
        validateDeletable();
        this.deleted = true;
    }

    // 검증 메서드
    private void validateParticipant(Member reader) {
        if (!chatRoom.hasParticipant(reader)) {
            throw new UnauthorizedMessageAccessException(this.id, reader.getId());
        }
    }

    private void validateSender(Member user) {
        if (!isSender(user)) {
            throw new UnauthorizedMessageModificationException(this.id, user.getId());
        }
    }

    private void validateEditable() {
        if (!this.edited) {
            throw new MessageNotEditableException(this.id);
        }
    }

    private void validateDeletable() {
        if (!this.deleted) {
            throw new MessageNotDeletableException(this.id);
        }
    }

    // 상태 확인 메서드
    public boolean isReadBy(Member user) {
        return this.readByUsers.containsKey(user.getId());
    }

    public boolean isSender(Member user) {
        return this.sender != null && this.sender.equals(user);
    }

    public boolean isSystem() {
        return this.type == MessageType.SYSTEM;
    }

    public boolean isDeleted() {
        return this.deleted;
    }
}
