package com.lolclone.matchmaking_domain.domain;

import com.lolclone.matchmaking_domain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_champion_pool")
public class MemberChampionPool extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "champion")
    private Champion champion; // 플레이어가 보유한 챔피언
    
    @Column(name = "mastery_level") // 챔피언 숙련도
    private Integer masteryLevel;

    @Column(name = "mastery_points") // 숙련도 포인트
    private Integer masteryPoints;

    @Column(name = "games_played") // 해당 챔피언 게임 수
    private Integer gamesPlayed;

    @Column(name = "win_rate") // 해당 챔피언 승률
    private Double winRate;
}
