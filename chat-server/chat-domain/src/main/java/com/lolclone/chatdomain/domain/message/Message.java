package com.lolclone.chatdomain.domain.message;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.lolclone.chatdomain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
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

    @Embedded
    private MessageContent content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "edited")
    private boolean edited;

    @ElementCollection
    @CollectionTable(
        name = "message_read_status",
        joinColumns = @JoinColumn(name = "message_id")
    )
    private Set<UUID> readByUsers;

    @Column(name = "deleted")
    private boolean deleted;

    @Builder
    private Message(ChatRoom chatRoom, Member sender, String content, MessageType type) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = MessageContent.of(content);
        this.type = type;
        this.sentAt = LocalDateTime.now();
        this.edited = false;
        this.readByUsers = new HashSet<>();
        this.deleted = false;
    }

    // 정적 팩토리 메서드
    public static Message create(ChatRoom chatRoom, Member sender, String content) {
        return new Message(chatRoom, sender, content, MessageType.TEXT);
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
        this.readByUsers.add(reader.getId().getValue());
    }

    public void edit(Member editor, String newContent) {
        validateSender(editor);
        validateEditable();
        this.content = MessageContent.of(newContent);
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
            throw new UnauthorizedMessageAccessException(this.id, reader.getId().getValue());
        }
    }

    private void validateSender(Member user) {
        if (!isSender(user)) {
            throw new UnauthorizedMessageModificationException(this.id, user.getId().getValue());
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
        return this.readByUsers.contains(user.getId().getValue());
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
