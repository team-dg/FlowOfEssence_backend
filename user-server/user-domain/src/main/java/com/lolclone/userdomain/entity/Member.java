package com.lolclone.userdomain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.UUID;

import com.lolclone.userdomain.common.BaseTimeEntity;
import com.lolclone.userdomain.exception.UnsupportedStateTransitionException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    private static final int MAX_NICKNAME_LENGTH = 10;

    @Id
    @Column(name = "user_id", columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    @Size(max = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberState state;

    @Enumerated(EnumType.STRING)
    private MemberState previousState;

    public Member(UUID userId, String nickname, MemberState state) {
        this.id = userId;
        this.nickname = nickname;
        this.state = state;
    }

    public static Member of(UUID userId, String nickname) {
        return new Member(userId, nickname, MemberState.PENDING);
    }

    public List<UserDomainEvent> undoCreateUser() {
        switch(state) {
            case ACTIVE:
            case PENDING:
                this.previousState = this.state;
                this.state = MemberState.DELETED;
                return singletonList(new UndoCreateUserEvent(this.id));
            case DELETED:
                throw new IllegalStateException("이미 삭제된 사용자입니다.");
            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }

    public List<UserDomainEvent> createUser() {
        switch(state) {
            case PENDING:
                this.previousState = this.state;
                this.state = MemberState.ACTIVE;
                return singletonList(new UserCreatedEvent(this.id));
            case ACTIVE:
                throw new IllegalStateException("이미 활성된 사용자입니다.");
            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }    
}
