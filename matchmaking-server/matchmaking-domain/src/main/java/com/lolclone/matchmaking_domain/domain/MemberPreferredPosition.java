package com.lolclone.matchmaking_domain.domain;

import com.lolclone.matchmaking_domain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_preferred_positions")
public class MemberPreferredPosition extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "position_type", nullable = false)
    private PositionType positionType;

    @Column(name = "preference_order") // 선호도 순서
    private Integer order;

    @Column(name = "games_played") // 해당 포지션 게임 수
    private Integer gamesPlayed;

    @Column(name = "win_rate") // 해당 포지션 승률
    private Double winRate;
}
