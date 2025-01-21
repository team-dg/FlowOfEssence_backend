package com.lolclone.chatdomain.domain.gameinvite;

import com.lolclone.chatdomain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.exception.InvalidGameInviteException;
import com.lolclone.chatdomain.exception.InvalidGameInviteStatusException;
import com.lolclone.chatdomain.exception.UnauthorizedGameInviteException;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_invites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameInvite extends BaseTimeEntity {
    @EmbeddedId
    private GameInviteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id")
    private Member inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id")
    private Member invitee;

    @Embedded
    private GameInviteStatus status;

    @Embedded
    private GameInviteMetadata metadata;

    private GameInvite(Member inviter, Member invitee) {
        validateInvite(inviter, invitee);
        this.id = GameInviteId.newId();
        this.inviter = inviter;
        this.invitee = invitee;
        this.status = GameInviteStatus.pending();
        this.metadata = GameInviteMetadata.init();
    }

    public static GameInvite create(Member inviter, Member invitee) {
        return new GameInvite(inviter, invitee);
    }

    public void accept() {
        validatePendingStatus();
        this.status = this.status.accept();
        this.metadata = this.metadata.updateProcessedTime();
    }

    public void reject() {
        validatePendingStatus();
        this.status = this.status.reject();
        this.metadata = this.metadata.updateProcessedTime();
    }

    public void cancel() {
        validatePendingStatus();
        validateInviter(inviter);
        this.status = this.status.cancel();
        this.metadata = this.metadata.updateProcessedTime();
    }

    public void expire() {
        if (this.status.isPending()) {
            this.status = this.status.expire();
            this.metadata = this.metadata.updateProcessedTime();
        }
    }

    private static void validateInvite(Member inviter, Member invitee) {
        if (inviter.equals(invitee)) {
            throw new InvalidGameInviteException("자기 자신을 게임에 초대할 수 없습니다.");
        }
        if (!inviter.isOnline() || !invitee.isOnline()) {
            throw new InvalidGameInviteException("오프라인 상태의 사용자와는 게임을 할 수 없습니다.");
        }
    }

    private void validatePendingStatus() {
        if (!this.status.isPending()) {
            throw new InvalidGameInviteStatusException("이미 처리된 게임 초대입니다.", this.id);
        }
    }

    private void validateInviter(Member member) {
        if (!this.inviter.equals(member)) {
            throw new UnauthorizedGameInviteException("게임 초대를 취소할 권한이 없습니다.", this.id);
        }
    }
}
