package com.lolclone.chatdomain.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 채팅방 참여 여부 확인
 * 채팅방 참여자 목록 조회
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_participants", 
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_chatroom_user", 
            columnNames = { "chat_room_id", "user_id" }
        )
    }
)
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    private Member user;

    @Column(name = "last_read_message_id")
    private Long lastReadMessageId;

    private boolean muted;

    private ChatParticipant(ChatRoom chatRoom, Member user) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.muted = false;
    }

    // 정적 팩토리 메서드
    public static ChatParticipant of(ChatRoom chatRoom, Member user) {
        return new ChatParticipant(chatRoom, user);
    }

    // 비즈니스 메서드
    public void updateLastReadMessage(Long messageId) {
        this.lastReadMessageId = messageId;
    }

    public void mute() {
        this.muted = true;
    }

    public void unmute() {
        this.muted = false;
    }

    // 조회 메서드
    public boolean hasUnreadMessages(Long latestMessageId) {
        return lastReadMessageId == null || latestMessageId > lastReadMessageId;
    }

    public boolean isMuted() {
        return muted;
    }
}
