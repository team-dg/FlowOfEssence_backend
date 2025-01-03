package com.lolclone.chat_server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.lolclone.chat_server.common.domain.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "folder_friends",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_folder_friend",
            columnNames = {"folder_id", "friend_id"}
        )
    }
)
public class FolderFriend extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "folder_friend_id", columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    private FriendFolder folder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private Friend friend;
    
    @Column(name = "order_index")
    private int orderIndex;
    
    private FolderFriend(FriendFolder folder, Friend friend) {
        this.folder = folder;
        this.friend = friend;
        this.orderIndex = folder.getFriends().size();
    }
    
    public static FolderFriend of(FriendFolder folder, Friend friend) {
        return new FolderFriend(folder, friend);
    }
    
    public void updateOrder(int newOrder) {
        this.orderIndex = newOrder;
    }
} 