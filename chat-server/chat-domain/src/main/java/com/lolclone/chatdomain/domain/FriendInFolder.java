package com.lolclone.chatdomain.domain;

import java.util.UUID;

import com.lolclone.chatdomain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.friend.Friend;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 특정 폴더에 속한 친구 목록 조회
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friend_in_folder",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_folder_friend", 
            columnNames = { "folder_id", "friend_id" }
        )
    }
)
public class FriendInFolder extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "friend_in_folder_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false, columnDefinition = "uuid")
    private FriendFolder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false, columnDefinition = "uuid")
    private Friend friend;

    private FriendInFolder(FriendFolder folder, Friend friend) {
        this.folder = folder;
        this.friend = friend;
    }

    public static FriendInFolder create(FriendFolder folder, Friend friend) {
        return new FriendInFolder(folder, friend);
    }
}
