package com.lolclone.chatdomain.domain.gameinvite;

import java.util.UUID;

import com.lolclone.chatdomain.domain.MemberStatus;
import com.lolclone.chatdomain.domain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.member.GameType;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.exception.InvalidGameInviteException;
import com.lolclone.chatdomain.exception.InvalidGameInviteStatusException;
import com.lolclone.chatdomain.exception.UnauthorizedGameInviteException;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_invites",
    indexes = {
        @Index(name = "idx_invitee_status", columnList = "invitee_id, status"),
        @Index(name = "idx_expires_at", columnList = "expires_at")
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameInvite extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "invite_id")
    private UUID id;

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

    @Column(name = "game_session_id") // ✅ 게임 세션 연결
    private UUID gameSessionId;

    @Column(name = "chat_room_id")    // ✅ 게임 채팅방 연결
    private UUID chatRoomId;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type")
    private GameType gameType;

    // public void deleteGameChatMessages(UUID gameSessionId) {
    //     messageRepository.deleteByGameSessionId(gameSessionId);
    // }

    private GameInvite(Member inviter, Member invitee) {
        validateInvite(inviter, invitee);
        this.inviter = inviter;
        this.invitee = invitee;
        this.status = GameInviteStatus.pending();
        this.metadata = GameInviteMetadata.init();
    }

    // @Scheduled(fixedRate = 300_000) // 5분마다 실행
    // public void expireOldInvites() {
    //     List<GameInvite> expired = repository.findByStatusAndExpiresAtBefore(
    //         Status.PENDING, LocalDateTime.now()
    //     );
    //     expired.forEach(GameInvite::expire);
    // }

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
        if (invitee.getStateInfo().getStatus() == MemberStatus.OFFLINE) {
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
