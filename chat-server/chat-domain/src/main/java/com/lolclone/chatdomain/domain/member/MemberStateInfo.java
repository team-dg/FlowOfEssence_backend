package com.lolclone.chatdomain.domain.member;

import static java.util.Collections.singletonList;

import java.util.List;

import com.lolclone.chatdomain.domain.MemberCreatedEvent;
import com.lolclone.chatdomain.domain.MemberDomainEvent;
import com.lolclone.chatdomain.domain.MemberState;
import com.lolclone.chatdomain.domain.MemberStatus;
import com.lolclone.chatdomain.domain.UndoCreateMemberEvent;
import com.lolclone.chatdomain.exception.UnsupportedStateTransitionException;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberStateInfo {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberState state;

    @Enumerated(EnumType.STRING)
    private MemberState previousState;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    public static MemberStateInfo init() {
        MemberStateInfo info = new MemberStateInfo();
        info.state = MemberState.PENDING;
        info.status = MemberStatus.OFFLINE;
        return info;
    }

    public List<MemberDomainEvent> activate(MemberId memberId) {
        validateStateTransition(MemberState.ACTIVE);
        this.previousState = this.state;
        this.state = MemberState.ACTIVE;
        return singletonList(new MemberCreatedEvent(memberId.getValue()));
    }

    public List<MemberDomainEvent> deactivate(MemberId memberId) {
        validateStateTransition(MemberState.DELETED);
        this.previousState = this.state;
        this.state = MemberState.DELETED;
        return singletonList(new UndoCreateMemberEvent(memberId.getValue()));
    }

    private void validateStateTransition(MemberState newState) {
        if (!canTransitionTo(newState)) {
            throw new UnsupportedStateTransitionException(this.state);
        }
    }

    private boolean canTransitionTo(MemberState newState) {
        return switch (this.state) {
            case PENDING -> newState == MemberState.ACTIVE;
            case ACTIVE -> newState == MemberState.DELETED;
            case DELETED -> false;
        };
    }

    public MemberStateInfo updateStatus(MemberStatus newStatus) {
        MemberStateInfo info = new MemberStateInfo();
        info.state = this.state;
        info.previousState = this.previousState;
        info.status = newStatus;
        return info;
    }

    public boolean isActive() {
        return this.state == MemberState.ACTIVE;
    }
}
