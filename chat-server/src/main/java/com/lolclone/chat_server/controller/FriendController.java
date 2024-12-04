package com.lolclone.chat_server.controller;

import com.lolclone.chat_server.dto.request.BlockFriendRequestDto;
import com.lolclone.chat_server.dto.request.DeleteFriendRequestDto;
import com.lolclone.chat_server.dto.request.FriendAcceptDto;
import com.lolclone.chat_server.dto.request.FriendRequestDto;
import com.lolclone.chat_server.dto.request.GetFriendInfoRequest;
import com.lolclone.chat_server.dto.request.MemoRequest;
import com.lolclone.chat_server.dto.request.FriendSearchRequest;
import com.lolclone.chat_server.dto.response.FriendRequestResponseDto;
import com.lolclone.chat_server.dto.response.FriendResponseDto;
import com.lolclone.chat_server.service.FriendService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    
    @GetMapping("/list")
    public ResponseEntity<List<FriendResponseDto>> getFriendsList(
            @Valid @RequestParam final Long userId) {
        final List<FriendResponseDto> friends = friendService.getFriendsList(userId);
        return ResponseEntity.ok().body(friends);
    }
    
    @GetMapping("/sort/alphabetical")
    public ResponseEntity<List<FriendResponseDto>> sortByAlphabetical(
            @Valid @RequestParam final Long userId) {
        final List<FriendResponseDto> friends = friendService.sortByAlphabetical(userId);
        return ResponseEntity.ok().body(friends);
    }
    
    @GetMapping("/sort/status")
    public ResponseEntity<List<FriendResponseDto>> sortByStatus(
            @Valid @RequestParam final Long userId) {
        final List<FriendResponseDto> friends = friendService.sortByStatus(userId);
        return ResponseEntity.ok().body(friends);
    }
    
    @PostMapping("/request")
    public ResponseEntity<Void> sendFriendRequest(
            @Valid @RequestBody final FriendRequestDto request) {
        friendService.sendFriendRequest(request.senderId(), request.receiverNickname());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/accept")
    public ResponseEntity<Void> acceptFriendRequest(
            @Valid @RequestBody final FriendAcceptDto request) {
        friendService.acceptFriendRequest(request.receiverId(), request.requesterId());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestResponseDto>> getPendingRequests(
            @Valid @RequestParam final Long userId) {
        final List<FriendRequestResponseDto> requests = friendService.getPendingRequests(userId);
        return ResponseEntity.ok().body(requests);
    }

    @PostMapping("/block")
    public ResponseEntity<Void> blockFriend(
            @Valid @RequestBody final BlockFriendRequestDto request) {
        friendService.blockFriend(request.userId(), request.friendId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unblock")
    public ResponseEntity<Void> unblockFriend(
            @Valid @RequestBody final BlockFriendRequestDto request) {
        friendService.unblockFriend(request.userId(), request.friendId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/memo")
    public ResponseEntity<Void> updateFriendMemo(
            @Valid @RequestBody final MemoRequest request) {
        friendService.updateFriendMemo(request.userId(), request.friendId(), request.memo());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFriend(
            @Valid @RequestBody final DeleteFriendRequestDto request) {
        friendService.deleteFriend(request.userId(), request.friendId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/info")
    public ResponseEntity<FriendResponseDto> getFriendInfo(
            @Valid @RequestBody final GetFriendInfoRequest request) {
        final FriendResponseDto friendInfo = friendService.getFriendInfo(request.userId(), request.friendId());
        return ResponseEntity.ok().body(friendInfo);
    }

    @PostMapping("/search")
    public ResponseEntity<List<FriendResponseDto>> searchFriends(
            @Valid @RequestBody final FriendSearchRequest request) {
        final List<FriendResponseDto> friends = friendService.searchFriendsInMessageWindow(request);
        return ResponseEntity.ok().body(friends);
    }
} 