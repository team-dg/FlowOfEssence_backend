package com.lolclone.chatdomain.domain;

import java.util.UUID;

import com.lolclone.chatdomain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 친구 폴더 정보 관리 (생성, 조회, 수정, 삭제)
 * 폴더 내 친구 목록 관리
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friend_folders")
public class FriendFolder extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "folder_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    private Member user;

    @Column(name = "folder_name", nullable = false)
    private String folderName;

    private FriendFolder(Member user, String folderName) {
        this.user = user;
        this.folderName = folderName;
    }

    public static FriendFolder create(Member user, String folderName) {
        return new FriendFolder(user, folderName);
    }

    public void updateFolderName(String folderName) {
        this.folderName = folderName;
    }
}
