package com.lolclone.matchmaking_domain.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.lolclone.matchmaking_domain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member extends BaseTimeEntity {
    private static final int MAX_NICKNAME_LENGTH = 10;

    @Id
    @Column(name = "user_id", columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Column(name = "summoner_id")
    private String summonerId; // 게임서버를 통해서 받아온 아이디를 저장
    
    @Column(name = "mmr")
    private Integer mmr;
    
    @Column(name = "lp") 
    private Integer lp;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tier")
    private Tier tier;

    @Enumerated(EnumType.STRING)
    @Column(name = "division")
    private Division division;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberPreferredPosition> preferredPositions;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberChampionPool> championPool;
    
    @Column(name = "behavior_score")
    private Integer behaviorScore;
    
    @Column(name = "last_match_time")
    private LocalDateTime lastMatchTime; // 닷지, 패널티 적용시 사용
}
