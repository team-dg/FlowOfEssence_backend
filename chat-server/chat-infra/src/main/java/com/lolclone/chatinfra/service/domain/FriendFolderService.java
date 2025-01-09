package com.lolclone.chatinfra.service.domain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.FriendFolder;
import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.repository.FriendFolderRepository;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FriendFolderService {
    private final FriendFolderRepository friendFolderRepository;

    public FriendFolder getOrThrow(final Long id) {
        return friendFolderRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_FOLDER_NOT_FOUND));
    }

    /**
     * 친구 폴더 생성
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public FriendFolder createFolder(final Member user, final String folderName) {
        FriendFolder folder = FriendFolder.create(user, folderName);
        return friendFolderRepository.save(folder);
    }

    /**
     * 친구 폴더 이름 수정
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateFolderName(final Long folderId, final String newFolderName) {
        FriendFolder folder = getOrThrow(folderId);
        folder.updateFolderName(newFolderName);
    }

    /**
     * 사용자의 모든 친구 폴더 조회
     */
    public List<FriendFolder> getUserFolders(final Member user) {
        return friendFolderRepository.findByUser(user);
    }

    /**
     * 폴더 소유자 확인
     */
    public boolean isOwner(final Long folderId, final Member user) {
        FriendFolder folder = getOrThrow(folderId);
        return folder.getUser().equals(user);
    }

    /**
     * 폴더 삭제
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteFolder(final Long folderId) {
        FriendFolder folder = getOrThrow(folderId);
        friendFolderRepository.delete(folder);
    }
}
