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
    /**
     * 마지막으로 읽은 메시지 ID를 업데이트합니다.
     * @param messageId 마지막으로 읽은 메시지 ID
     */
    public void updateLastReadMessage(Long messageId) {
        this.lastReadMessageId = messageId;
    }

    /**
     * 채팅 참가자를 뮤트합니다.
     */
    public void mute() {
        this.muted = true;
    }

    /**
     * 채팅 참가자를 언뮤트합니다.
     */
    public void unmute() {
        this.muted = false;
    }

    // 조회 메서드
    /**
     * 읽지 않은 메시지가 있는지 확인합니다.
     * @param latestMessageId 가장 최근 메시지 ID
     * @return 읽지 않은 메시지가 있으면 true, 없으면 false
     */
    public boolean hasUnreadMessages(Long latestMessageId) {
        return lastReadMessageId == null || latestMessageId > lastReadMessageId;
    }

    /**
     * 뮤트 상태인지 확인합니다.
     * @return 뮤트 상태이면 true, 아니면 false
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * 읽지 않은 메시지 수를 계산
     * @param totalMessageCount 채팅방의 전체 메시지 수
     * @return 읽지 않은 메시지 수
     */
    public int getUnreadMessageCount(long totalMessageCount) {
        if (lastReadMessageId == null) {
            return (int) totalMessageCount;
        }
        return (int) (totalMessageCount - lastReadMessageId);
    }
}
