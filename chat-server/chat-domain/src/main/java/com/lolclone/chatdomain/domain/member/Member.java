package com.lolclone.chatdomain.domain.member;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lolclone.chatdomain.common.BaseEntity;
import com.lolclone.chatdomain.domain.MemberDomainEvent;
import com.lolclone.chatdomain.domain.MemberStatus;

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
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @EmbeddedId
    private MemberId id; // UUID를 값 객체로 변환

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @ElementCollection
    @CollectionTable(
        name = "member_tags",
        joinColumns = @JoinColumn(name = "member_id"),
        indexes = @Index(name = "idx_member_tags", columnList = "tag")
    )
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    @Column(name = "last_active_time")
    private LocalDateTime lastActiveTime;

    @Column(name = "online", nullable = false)
    private boolean online;

    @Embedded
    private MemberStateInfo stateInfo; // 상태 관련 정보를 값 객체로 분리

    @Column(name = "game_info")
    private GameInfo gameInfo;

    @Builder
    private Member(MemberId id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        this.tags = new HashSet<>();
        this.online = false;
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
        this.lastActiveTime = LocalDateTime.now();
    }

    public void endGame() {
        this.gameInfo = GameInfo.empty();
        this.stateInfo = this.stateInfo.updateStatus(MemberStatus.ONLINE);
        this.lastActiveTime = LocalDateTime.now();
    }

    public String getGameStatusDisplay() {
        return gameInfo != null ? gameInfo.getDisplayText() : null;
    }

    public void updateGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        this.lastActiveTime = LocalDateTime.now();
    }

    public void updateLoginStatus(boolean online) {
        this.online = online;
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
        return this.online;
    }
}
