package com.lolclone.chatdomain.domain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.lolclone.chatdomain.common.BaseEntity;
import com.lolclone.chatdomain.exception.UnsupportedStateTransitionException;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Collections.singletonList;

import java.util.HashSet;

/**
 * Entity 책임
 * 사용자 상태 관리
 * 사용자 마지막 로그인 시간 관리
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "users", 
    indexes = {
        @Index(name = "idx_nickname", columnList = "nickname")
    }
)
public class Member extends BaseEntity {
    private static final int MAX_NICKNAME_LENGTH = 10;

    @Id
    @Column(name = "user_id", columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status = MemberStatus.OFFLINE;

    @ElementCollection
    @CollectionTable(
        name = "member_tags", 
        joinColumns = @JoinColumn(name = "member_id"), 
        indexes = @Index(name = "idx_member_tags", columnList = "tag")
    )
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    @Column(length = 30)
    private String lastLogin;

    @Column(nullable = false)
    private boolean online;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberState state = MemberState.PENDING;

    @Enumerated(EnumType.STRING)
    private MemberState previousState;

    @Builder
    private Member(UUID id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        this.online = false;
    }

    public static Member of(UUID id, String nickname) {
        return new Member(id, nickname);
    }

    public void updateStatus(MemberStatus status) {
        this.status = status;
    }

    public void updateLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<MemberDomainEvent> createMember() {
        switch(state) {
            case PENDING:
                this.previousState = this.state;
                this.state = MemberState.ACTIVE;
                return singletonList(new MemberCreatedEvent(this.id));
            case ACTIVE:
                throw new IllegalStateException("이미 활성된 사용자입니다.");
            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }

    public List<MemberDomainEvent> undoCreateMember() {
        switch(state) {
            case ACTIVE:
            case PENDING:
                this.previousState = this.state;
                this.state = MemberState.DELETED;
                return singletonList(new UndoCreateMemberEvent(this.id));
            case DELETED:
                throw new IllegalStateException("이미 삭제된 사용자입니다.");
            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }

    public void setOnline(boolean status) {
        this.online = status;
    }

    public boolean hasTag(String tag) {
        return tags.stream()
                .anyMatch(t -> t.toLowerCase().contains(tag.toLowerCase()));
    }
}
