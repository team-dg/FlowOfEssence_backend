package com.lolclone.chatdomain.domain.chatparticipant;

import java.util.List;

import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.domain.message.Message;
import com.lolclone.chatdomain.exception.InvalidParticipantException;

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
@Table(name = "chat_participants")
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatParticipant {
    @EmbeddedId
    private ChatParticipantId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member user;

    @Embedded
    private LastReadMessage lastReadMessage;

    @Embedded
    private ParticipantStatus status;

    private ChatParticipant(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.user = member;
        this.lastReadMessage = LastReadMessage.init();
        this.status = ParticipantStatus.active();
    }

    // 정적 팩토리 메서드
    public static ChatParticipant join(ChatRoom chatRoom, Member user) {
        ChatParticipant participant = new ChatParticipant(chatRoom, user);
        chatRoom.addParticipantDirectly(participant); // 양방향 연관관계 설정
        return participant;
    }

    // 비즈니스 메서드
    
    public void readMessage(Message message) {
        validateParticipant(message.getChatRoom());
        this.lastReadMessage = LastReadMessage.of(message);
    }

    public void mute() {
        this.status = this.status.mute();
    }

    public void unmute() {
        this.status = this.status.unmute();
    }

    public void leave() {
        validateActiveParticipant();
        this.status = this.status.leave();
        this.chatRoom.removeParticipantDirectly(this);
    }

    // 검증 메서드
    private void validateParticipant(ChatRoom chatRoom) {
        if (!this.chatRoom.equals(chatRoom)) {
            throw new InvalidParticipantException("참여하지 않은 채팅방입니다.");
        }
    }

    private void validateActiveParticipant() {
        if (!this.status.isActive()) {
            throw new InvalidParticipantException("활성 상태가 아닌 참여자입니다.");
        }
    }

    // 비즈니스 로직 메서드
    public boolean hasUnreadMessages(Message latestMessage) {
        return this.lastReadMessage.isOlderThan(latestMessage);
    }

    public int calculateUnreadCount(List<Message> messages) {
        return this.lastReadMessage.calculateUnreadCount(messages);
    }

    public boolean isMuted() {
        return this.status.isMuted();
    }

    public Member getUser() {
        return this.user;
    }
}
