package com.lolclone.chatinfra.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.Friend;
import com.lolclone.chatdomain.domain.FriendRequest;
import com.lolclone.chatdomain.domain.Message;
import com.lolclone.chatinfra.service.application.ChatService;
import com.lolclone.chatserviceapi.dto.FriendChatInfoDto;
import com.lolclone.chatserviceapi.dto.MessageRequestDto;
import com.lolclone.chatserviceapi.dto.UserSearchResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    /**
     * 1대1 채팅방 생성 또는 조회
     */
    @PostMapping("/rooms/personal/{friendId}")
    public ResponseEntity<ChatRoom> getOrCreatePersonalChatRoom(
        @RequestParam("userId") final UUID userId,
        @PathVariable final UUID friendId) {
        ChatRoom chatRoom = chatService.getOrCreatePersonalChatRoom(userId, friendId);
        return ResponseEntity.ok().body(chatRoom);
    }

    /**
     * 날짜별 채팅 메시지 조회
     */
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<Message>> getChatMessagesByDate(
            @PathVariable final UUID roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date) {
        List<Message> messages = chatService.getChatMessagesByDate(roomId, date);
        return ResponseEntity.ok().body(messages);
    }

    /**
     * 1대1 채팅 메시지 전송
     */
    @MessageMapping("/chat/private/{roomId}")
    public void sendPersonalChatMessage(
            @RequestParam("userId") final UUID userId,
            @PathVariable final UUID roomId,
            @Payload final MessageRequestDto request) {
        chatService.sendPersonalChatMessage(roomId, userId, request.content());
    }

    /**
     * 1대1 채팅방의 모든 대화 기록 삭제
     */
    @DeleteMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Void> deleteAllPersonalChatMessages(
            @RequestParam("userId") final UUID userId,
            @PathVariable final UUID roomId) {
        chatService.deleteAllPersonalChatMessages(roomId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자의 친구 목록을 조회 (정렬 옵션 지원)
     * 차단된 친구는 목록에서 제외됩니다.
     */
    @GetMapping("/friends")
    public ResponseEntity<List<FriendChatInfoDto>> getFriendListSortedByNickname(
            @RequestParam("userId") final UUID userId) {
        List<FriendChatInfoDto> friendChatInfos = chatService.getFriendList(userId, true);
        return ResponseEntity.ok().body(friendChatInfos);
    }

    /**
     * 친구 추가
     */
    @PostMapping("/friends/{friendId}")
    public ResponseEntity<Void> addFriend(
            @RequestParam("userId") final UUID userId,
            @PathVariable final UUID friendId) {
        chatService.sendFriendRequest(userId, friendId);
        return ResponseEntity.ok().build();
    }

    /**
     * 닉네임으로 사용자 검색
     */
    @GetMapping("/users/search")
    public ResponseEntity<List<UserSearchResponseDto>> searchUsers(
            @RequestParam("userId") UUID userId,
            @RequestParam String nickname) {
        List<UserSearchResponseDto> users = chatService.searchUsersByNickname(userId, nickname);
        return ResponseEntity.ok(users);
    }

    /**
     * 친구 목록 상태별 조회 (온라인/오프라인 등)
     */
    @GetMapping("/friends/by-status")
    public ResponseEntity<List<FriendChatInfoDto>> getFriendListSortedByStatus(
            @RequestParam("userId") final UUID userId) {
        List<FriendChatInfoDto> friendChatInfos = chatService.getFriendListSortedByStatus(userId);
        return ResponseEntity.ok(friendChatInfos);
    }

    /**
     * 친구 요청 보내기
     */
    @PostMapping("/friend-requests/{receiverId}")
    public ResponseEntity<FriendRequest> sendFriendRequest(
            @RequestParam("userId") final UUID userId,
            @PathVariable final UUID receiverId) {
        chatService.sendFriendRequest(userId, receiverId);
        return ResponseEntity.ok().build();
    }

    /**
     * 친구 요청 수락
     */
    @PostMapping("/friend-requests/{requestId}/accept")
    public ResponseEntity<Friend> acceptFriendRequest(
            @PathVariable final Long requestId) {
        Friend friend = chatService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().body(friend);
    }

    /**
     * 친구 요청 거절
     */
    @PostMapping("/friend-requests/{requestId}/reject")
    public ResponseEntity<Void> rejectFriendRequest(
            @PathVariable final Long requestId) {
        chatService.rejectFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    /**
     * 받은 친구 요청 목록 조회
     */
    @GetMapping("/friend-requests/received")
    public ResponseEntity<List<FriendRequest>> getReceivedFriendRequests(
            @RequestParam("userId") final UUID userId) {
        List<FriendRequest> requests = chatService.getReceivedFriendRequests(userId);
        return ResponseEntity.ok().body(requests);
    }
}
