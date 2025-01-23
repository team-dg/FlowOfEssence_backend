package com.lolclone.chatdomain.domain.member;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.lolclone.chatdomain.domain.MemberDomainEvent;
import com.lolclone.chatdomain.domain.MemberStatus;
import com.lolclone.chatdomain.domain.common.BaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 사용자 상태 관리
 * 사용자 마지막 로그인 시간 관리
 */
@Entity
@Table(name = "users",
indexes = {
    @Index(name = "idx_nickname", columnList = "nickname"),
    @Index(name = "idx_status", columnList = "status")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    private UUID id;

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @ElementCollection
    @CollectionTable(name = "member_tags", joinColumns = @JoinColumn(name = "member_id"))
    private Set<String> tags;

    @Embedded
    private MemberStateInfo stateInfo; // 상태 관련 정보를 값 객체로 분리

    @Column(name = "game_info")
    private GameInfo gameInfo;

    @ElementCollection
    @CollectionTable(name = "blocked_users", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "blocked_user_id")
    private Set<UUID> blockedUserIds = new HashSet<>();

    @Column(name = "current_lobby_participants")
    private Integer currentLobbyParticipants;

    public void blockUser(UUID userId) {
        blockedUserIds.add(userId);
    }

    public void unblockUser(UUID userId) {
        blockedUserIds.remove(userId);
    }

    @Builder
    private Member(UUID id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        this.tags = new HashSet<>();
        this.stateInfo = MemberStateInfo.init();
    }

    // 비즈니스 메서드
    public List<MemberDomainEvent> activate() {
        return this.stateInfo.activate(this.id);
    }

    public List<MemberDomainEvent> deactivate() {
        return this.stateInfo.deactivate(this.id);
    }

    public void startGame(GameType gameType, GameMode gameMode) {
        this.gameInfo = GameInfo.createGameInfo(gameType, gameMode);
        this.stateInfo = this.stateInfo.updateStatus(MemberStatus.IN_GAME);
    }

    public void endGame() {
        this.gameInfo = GameInfo.empty();
        this.stateInfo = this.stateInfo.updateStatus(MemberStatus.ONLINE);
    }

    public String getGameStatusDisplay() {
        return gameInfo != null ? gameInfo.getDisplayText() : null;
    }

    public void updateGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void updateLoginStatus(boolean online) {
        this.stateInfo = this.stateInfo.updateStatus(
                online ? MemberStatus.ONLINE : MemberStatus.OFFLINE);
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    // 상태 확인 메서드
    public boolean isActive() {
        return this.stateInfo.isActive();
    }

    public boolean hasTag(String tag) {
        return this.tags.stream()
                .anyMatch(t -> t.toLowerCase().contains(tag.toLowerCase()));
    }

    public boolean isOnline() {
        return this.stateInfo.getStatus() == MemberStatus.ONLINE;
    }
}
