package com.lolclone.chatdomain.domain;

import java.util.ArrayList;
import java.util.List;

import com.lolclone.chatdomain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 채팅방 정보 관리(생성, 조회, 수정, 삭제)
 * 채팅방 참여자 관리
 * 채팅방 메시지 관리
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_rooms")
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id")
    private Message lastMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private Member createdBy;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatParticipant> participants = new ArrayList<>();

    private boolean active = true;

    private ChatRoom(ChatRoomType type, Member createdBy) {
        this.type = type;
        this.createdBy = createdBy;
    }

    // 정적 팩토리 메서드
    public static ChatRoom createPersonalRoom(Member user1, Member user2) {
        ChatRoom room = new ChatRoom(ChatRoomType.PERSONAL, user1);
        room.addParticipant(user1);
        room.addParticipant(user2);
        return room;
    }

    public static ChatRoom createGroupRoom(Member creator) {
        ChatRoom room = new ChatRoom(ChatRoomType.GROUP, creator);
        room.addParticipant(creator);
        return room;
    }

    // 비즈니스 메서드
    public void updateLastMessage(Message message) {
        this.lastMessage = message;
    }

    public void addParticipant(Member member) {
        ChatParticipant participant = ChatParticipant.of(this, member);
        this.participants.add(participant);
    }

    public void removeParticipant(Member member) {
        this.participants.removeIf(p -> p.getUser().equals(member));
    }

    // 채팅방 활성화 여부
    public void deactivate() {
        this.active = false;
    }

    // 조회 메서드
    public boolean isPersonal() {
        return this.type == ChatRoomType.PERSONAL;
    }

    public boolean isGroup() {
        return this.type == ChatRoomType.GROUP;
    }

    public boolean hasParticipant(Member member) {
        return this.participants.stream()
                .anyMatch(p -> p.getUser().equals(member));
    }
}
