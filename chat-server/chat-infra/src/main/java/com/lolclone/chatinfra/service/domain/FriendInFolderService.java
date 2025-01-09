package com.lolclone.chatinfra.service.domain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.Friend;
import com.lolclone.chatdomain.domain.FriendFolder;
import com.lolclone.chatdomain.domain.FriendInFolder;
import com.lolclone.chatdomain.repository.FriendInFolderRepository;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FriendInFolderService {
    private final FriendInFolderRepository friendInFolderRepository;

    public FriendInFolder getOrThrow(final Long id) {
        return friendInFolderRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_IN_FOLDER_NOT_FOUND));
    }

    /**
     * 폴더에 친구 추가
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public FriendInFolder addFriendToFolder(final FriendFolder folder, final Friend friend) {
        FriendInFolder friendInFolder = FriendInFolder.create(folder, friend);
        return friendInFolderRepository.save(friendInFolder);
    }

    /**
     * 폴더에서 친구 제거
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeFriendFromFolder(final Long friendInFolderId) {
        FriendInFolder friendInFolder = getOrThrow(friendInFolderId);
        friendInFolderRepository.delete(friendInFolder);
    }

    /**
     * 특정 폴더의 모든 친구 목록 조회
     */
    public List<FriendInFolder> getFriendsInFolder(final FriendFolder folder) {
        return friendInFolderRepository.findByFolder(folder);
    }

    /**
     * 특정 친구가 폴더에 있는지 확인
     */
    public boolean isFriendInFolder(final FriendFolder folder, final Friend friend) {
        return friendInFolderRepository.findByFolderAndFriend(folder, friend).isPresent();
    }

    /**
     * 특정 친구의 모든 폴더 목록 조회
     */
    public List<FriendInFolder> getFoldersByFriend(final Friend friend) {
        return friendInFolderRepository.findByFriend(friend);
    }
}
