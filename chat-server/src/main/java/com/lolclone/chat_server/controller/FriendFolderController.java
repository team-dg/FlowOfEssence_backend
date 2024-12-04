package com.lolclone.chat_server.controller;

import com.lolclone.chat_server.dto.request.FolderCreateRequest;
import com.lolclone.chat_server.dto.request.FolderFriendRequest;
import com.lolclone.chat_server.dto.request.UpdateFolderNameRequest;
import com.lolclone.chat_server.dto.request.UpdateFriendOrderRequest;
import com.lolclone.chat_server.dto.request.DeleteFolderRequest;
import com.lolclone.chat_server.dto.response.FriendFolderResponseDto;
import com.lolclone.chat_server.service.FriendFolderService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/folders")
public class FriendFolderController {
    
    private final FriendFolderService folderService;
    
    @PostMapping
    public ResponseEntity<Long> createFolder(@Valid @RequestBody final FolderCreateRequest request) {
        final Long folderId = folderService.createFolder(request);
        return ResponseEntity.ok().body(folderId);
    }
    
    @PostMapping("/manage")
    public ResponseEntity<Void> manageFolderFriend(@Valid @RequestBody final FolderFriendRequest request) {
        folderService.manageFolderFriend(request);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping
    public ResponseEntity<List<FriendFolderResponseDto>> getFolders(@RequestParam final Long userId) {
        final List<FriendFolderResponseDto> folders = folderService.getFolders(userId);
        return ResponseEntity.ok().body(folders);
    }
    
    @PutMapping("/name")
    public ResponseEntity<Void> updateFolderName(
            @Valid @RequestBody final UpdateFolderNameRequest request) {
        folderService.updateFolderName(request.folderId(), request.newName(), request.userId());
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteFolder(@Valid @RequestBody final DeleteFolderRequest request) {
        folderService.deleteFolder(request.folderId(), request.userId());
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/friend-order")
    public ResponseEntity<Void> updateFriendOrder(
            @Valid @RequestBody final UpdateFriendOrderRequest request) {
        folderService.updateFriendOrder(request.folderId(), request.friendId(), request.newOrder());
        return ResponseEntity.ok().build();
    }
} 