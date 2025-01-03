package com.lolclone.chat_server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.lolclone.chat_server.common.domain.BaseTimeEntity;
import com.lolclone.chat_server.exception.common.NotFoundException;
import com.lolclone.chat_server.exception.domain.ExceptionType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friend_folders")
public class FriendFolder extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "friend_folder_id", columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FolderFriend> friends = new HashSet<>();
    
    private FriendFolder(User user, String name) {
        this.user = user;
        this.name = name;
    }
    
    public static FriendFolder of(User user, String name) {
        return new FriendFolder(user, name);
    }
    
    public void updateName(String name) {
        this.name = name;
    }

    public UUID getUserId() {
        return user.getId();
    }
    
    public void addFriend(Friend friend) {
        FolderFriend folderFriend = FolderFriend.of(this, friend);
        this.friends.add(folderFriend);
    }
    
    public void removeFriend(Friend friend) {
        this.friends.removeIf(folderFriend -> 
            folderFriend.getFriend().getId().equals(friend.getId()));
    }
    
    public void updateFriendOrder(UUID friendId, int newOrder) {
        FolderFriend targetFriend = this.friends.stream()
            .filter(ff -> ff.getFriend().getId().equals(friendId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_FRIEND));
            
        targetFriend.updateOrder(newOrder);
        
        // 다른 친구들의 순서도 조정
        this.friends.stream()
            .filter(ff -> !ff.getFriend().getId().equals(friendId))
            .filter(ff -> ff.getOrderIndex() >= newOrder)
            .forEach(ff -> ff.updateOrder(ff.getOrderIndex() + 1));
    }
} 