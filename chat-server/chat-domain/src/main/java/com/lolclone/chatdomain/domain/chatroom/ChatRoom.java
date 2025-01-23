package com.lolclone.chatdomain.domain.chatroom;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.lolclone.chatdomain.domain.chatparticipant.ChatParticipant;
import com.lolclone.chatdomain.domain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.domain.message.Message;
import com.lolclone.chatdomain.exception.DuplicateParticipantException;
import com.lolclone.chatdomain.exception.InactiveChatRoomException;
import com.lolclone.chatdomain.exception.InvalidChatRoomCreationException;
import com.lolclone.chatdomain.exception.ParticipantNotFoundException;
import com.lolclone.chatdomain.exception.TooManyParticipantsException;
import com.lolclone.chatdomain.exception.UnauthorizedParticipantException;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 채팅방 정보 관리(생성, 조회, 수정, 삭제)
 * 채팅방 참여자 관리
 * 채팅방 메시지 관리
 */
@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "chat_room_id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType type;

    @Embedded
    private ChatRoomStatus status; // 채팅방 상태 값 객체

    @Embedded
    private LastMessage lastMessage; // 마지막 메시지 값 객체

    @Column(name = "game_session_id")
    private UUID gameSessionId; // 게임 채팅방 연동

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatParticipant> participants;

    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Message> messages;

    @ElementCollection
    @CollectionTable(
        name = "chat_room_teams", 
        joinColumns = @JoinColumn(name = "room_id")
    )
    private Map<Member, TeamColor> teams;

    // 생성자는 private으로 제한
    @Builder
    private ChatRoom(ChatRoomType type) {
        this.type = type;
        this.status = ChatRoomStatus.active();
        this.lastMessage = LastMessage.empty();
        this.participants = new HashSet<>();
        this.messages = new ArrayList<>();
        this.teams = new HashMap<>();
    }

    // 정적 팩토리 메서드
    public static ChatRoom createPersonalRoom(Member user1, Member user2) {
        validatePersonalRoomCreation(user1, user2);
        ChatRoom room = new ChatRoom(ChatRoomType.PERSONAL);
        room.addParticipant(user1);
        room.addParticipant(user2);
        return room;
    }

    public static ChatRoom createGroupRoom(Member creator, String name) {
        ChatRoom room = new ChatRoom(ChatRoomType.GROUP);
        room.addParticipant(creator);
        return room;
    }

    // 비즈니스 메서드
    public Message sendMessage(Member sender, String content) {
        validateActiveRoom();
        validateParticipant(sender);

        Message message = Message.create(this, sender, content, Duration.ofMinutes(10));
        messages.add(message);
        this.lastMessage = LastMessage.from(message.getId());

        return message;
    }

    public void addParticipant(Member member) {
        validateActiveRoom();
        validateNewParticipant(member);

        ChatParticipant participant = ChatParticipant.join(this, member);
        participants.add(participant);
    }

    public void addParticipantDirectly(ChatParticipant participant) {
        participants.add(participant);
    }

    public void removeParticipantDirectly(ChatParticipant participant) {
        participants.remove(participant);
    }

    public void removeParticipant(Member member) {
        validateActiveRoom();
        ChatParticipant participant = findParticipantOrThrow(member);
        participant.leave();

        if (isPersonal() || participants.isEmpty()) {
            deactivate();
        }
    }

    public void clearMessages(Member requester) {
        validateActiveRoom();
        validateParticipant(requester);

        messages.clear();
        this.lastMessage = LastMessage.empty();
    }

    // 검증 메서드
    private void validateActiveRoom() {
        if (!status.isActive()) {
            throw new InactiveChatRoomException(this.id);
        }
    }

    private void validateParticipant(Member member) {
        if (!hasParticipant(member)) {
            throw new UnauthorizedParticipantException(member.getId(), this.id);
        }
    }

    private void validateNewParticipant(Member member) {
        if (hasParticipant(member)) {
            throw new DuplicateParticipantException(member.getId(), this.id);
        }
        if (isPersonal() && participants.size() >= 2) {
            throw new TooManyParticipantsException(this.id);
        }
    }

    private static void validatePersonalRoomCreation(Member user1, Member user2) {
        if (user1.equals(user2)) {
            throw new InvalidChatRoomCreationException("동일한 사용자와 1:1 채팅방을 생성할 수 없습니다");
        }
    }

    // 상태 변경 메서드
    private void deactivate() {
        this.status = this.status.deactivate();
    }

    public boolean isPersonal() {
        return this.type == ChatRoomType.PERSONAL;
    }

    // 조회 메서드
    public boolean hasParticipant(Member member) {
        return participants.stream()
                .anyMatch(p -> p.getUser().equals(member));
    }

    private ChatParticipant findParticipantOrThrow(Member member) {
        return participants.stream()
                .filter(p -> p.getUser().equals(member))
                .findFirst()
                .orElseThrow(() -> new ParticipantNotFoundException(member.getId(), this.id));
    }

    public enum TeamColor{
        RED,
        BLUE
    }
}
