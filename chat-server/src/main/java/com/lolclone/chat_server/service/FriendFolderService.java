package com.lolclone.chat_server.service;

import com.lolclone.chat_server.domain.Friend;
import com.lolclone.chat_server.domain.FriendFolder;
import com.lolclone.chat_server.domain.User;
import com.lolclone.chat_server.dto.request.FolderCreateRequest;
import com.lolclone.chat_server.dto.request.FolderFriendRequest;
import com.lolclone.chat_server.dto.response.FriendFolderResponseDto;
import com.lolclone.chat_server.exception.common.BadRequestException;
import com.lolclone.chat_server.exception.common.NotFoundException;
import com.lolclone.chat_server.exception.domain.ExceptionType;
import com.lolclone.chat_server.repository.FriendFolderRepository;
import com.lolclone.chat_server.repository.FriendRepository;
import com.lolclone.chat_server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendFolderService {
    
    private final FriendFolderRepository folderRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public UUID createFolder(final FolderCreateRequest request) {
        final User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));

        if (folderRepository.existsByUser_IdAndName(request.userId(), request.folderName())) {
            throw new BadRequestException(ExceptionType.DUPLICATE_FOLDER_NAME);
        }
        
        final FriendFolder folder = FriendFolder.of(user, request.folderName());
        
        return folderRepository.save(folder).getId();
    }
    
    @Transactional
    public void manageFolderFriend(final FolderFriendRequest request) {
        final FriendFolder folder = folderRepository.findByIdWithFriends(request.folderId())
            .orElseThrow(() -> new NotFoundException(ExceptionType.FOLDER_NOT_FOUND));
            
        final Friend friend = friendRepository.findByUserIdAndFriendId(folder.getUserId(), request.friendId())
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_FRIEND));
            
        if ("add".equals(request.action())) {
            folder.addFriend(friend);
        } else {
            folder.removeFriend(friend);
        }
    }
    
    public List<FriendFolderResponseDto> getFolders(final UUID userId) {
        return folderRepository.findByUserId(userId)
            .stream()
            .map(FriendFolderResponseDto::from)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void updateFolderName(final UUID folderId, final String newName, final UUID userId) {
        final FriendFolder folder = folderRepository.findById(folderId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.FOLDER_NOT_FOUND));
            
        if (!folder.getUserId().equals(userId)) {
            throw new BadRequestException(ExceptionType.INVALID_REQUEST_ARGUMENT);
        }
        
        if (folderRepository.existsByUser_IdAndName(userId, newName)) {
            throw new BadRequestException(ExceptionType.DUPLICATE_FOLDER_NAME);
        }
        
        folder.updateName(newName);
    }
    
    @Transactional
    public void deleteFolder(final UUID folderId, final UUID userId) {
        final FriendFolder folder = folderRepository.findById(folderId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.FOLDER_NOT_FOUND));
            
        if (!folder.getUserId().equals(userId)) {
            throw new BadRequestException(ExceptionType.INVALID_REQUEST_ARGUMENT);
        }
        
        folderRepository.delete(folder);
    }
    
    @Transactional
    public void updateFriendOrder(final UUID folderId, final UUID friendId, final int newOrder) {
        final FriendFolder folder = folderRepository.findByIdWithFriends(folderId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.FOLDER_NOT_FOUND));
            
        folder.updateFriendOrder(friendId, newOrder);
    }
} 
