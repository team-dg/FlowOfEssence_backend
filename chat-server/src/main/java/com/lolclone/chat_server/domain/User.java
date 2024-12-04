package com.lolclone.chat_server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.lolclone.chat_server.common.domain.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_nickname", columnList = "nickname"),
        @Index(name = "idx_nickname_tag", columnList = "nickname,tag"),
        @Index(name = "idx_nickname_tag_status", columnList = "nickname,tag,status")
    }
)
public class User extends BaseTimeEntity {
    @Id
    private Long id; // authentication-server에서 생성된 ID를 그대로 사용
    
    @Column(nullable = false, length = 30)
    private String nickname;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.OFFLINE;
    
    @Column(nullable = false, length = 10)
    private String tag = "KR1"; // 기본값 설정
    
    @Column(length = 100)
    private String gameInfo;
    
    private User(Long id, String nickname, String tag) {
        this.id = id;
        this.nickname = nickname;
        this.tag = tag;
    }
    
    public static User of(Long id, String nickname, String tag) {
        return new User(id, nickname, tag);
    }
    
    public void updateStatus(UserStatus status) {
        this.status = status;
    }
    
    public void updateGameInfo(String gameInfo) {
        this.gameInfo = gameInfo;
    }
} 
