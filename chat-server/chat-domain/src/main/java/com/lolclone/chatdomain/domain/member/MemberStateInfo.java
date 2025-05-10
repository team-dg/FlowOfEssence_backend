package com.lolclone.chatdomain.domain.member;

import com.lolclone.chatdomain.domain.MemberState;
import com.lolclone.chatdomain.domain.MemberStatus;

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
