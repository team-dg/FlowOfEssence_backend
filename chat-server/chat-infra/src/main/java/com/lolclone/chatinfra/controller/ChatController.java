package com.lolclone.chatinfra.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lolclone.chatdomain.repository.friend.query.FriendChatInfoDto;
import com.lolclone.chatinfra.service.application.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    /**
     * 📋 닉네임 기준으로 친구 목록을 페이징하여 조회 (정렬 옵션 지원)
     * ⚠️ 최대 친구 수: 300명
     * 🚫 차단된 친구는 목록에서 제외
     */
    @GetMapping("/friends/by-nickname")
    public ResponseEntity<Page<FriendChatInfoDto>> getFriendListSortedByNickname(
        @RequestParam("userId") final UUID userId,
        @RequestParam(defaultValue = "false") final boolean sortByNickname,
        @PageableDefault(size = 20) final Pageable pageable
    ) {
        Page<FriendChatInfoDto> friendChatInfos = chatService.getFriendListByNickname(userId, sortByNickname, pageable);
        return ResponseEntity.ok().body(friendChatInfos);
    }

    /**
     * 📋 온라인 상태 기준으로 친구 목록을 페이징하여 조회 (정렬 옵션 지원)
     * ⚠️ 최대 친구 수: 300명
     * 🚫 차단된 친구는 목록에서 제외
     * 🟢 온라인 상태 정렬 여부 선택 가능
     */
    // @GetMapping("/friends/by-status")
    // public ResponseEntity<Page<FriendChatInfoDto>> getFriendListByStatus(
    //         @RequestParam("userId") final UUID userId,
    //         @RequestParam(defaultValue = "false") final boolean sortByStatus,
    //         @PageableDefault(size = 20) final Pageable pageable) {
    //     Page<FriendChatInfoDto> friendChatInfos = chatService.getFriendListByStatus(userId, sortByStatus, pageable);
    //     return ResponseEntity.ok().body(friendChatInfos);
    // }

    // /**
    // * 친구 목록 상태별 조회 (온라인/오프라인 등)
    // */
    // @GetMapping("/friends/by-status")
    // public ResponseEntity<List<FriendChatInfoDto>> getFriendListSortedByStatus(
    // @RequestParam("userId") final UUID userId) {
    // List<FriendChatInfoDto> friendChatInfos =
    // chatService.getFriendListSortedByStatus(userId);
    // return ResponseEntity.ok(friendChatInfos);
    // }


    // /**
    //  * 1대1 채팅방 생성 또는 조회
    //  */
    // @GetMapping("/rooms/personal/{friendId}")
    // public ResponseEntity<ChatRoom> getOrCreatePersonalChatRoom(
    //     @RequestParam final UUID userId,
    //     @PathVariable final UUID friendId) {
    //     return ResponseEntity.ok(chatService.getOrCreatePersonalChatRoom(userId, friendId));
    // }

    // /**
    //  * 날짜별 채팅 메시지 조회
    //  */
    // @GetMapping("/rooms/{roomId}/messages")
    // public ResponseEntity<List<Message>> getChatMessagesByDate(
    //         @PathVariable final UUID roomId,
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date) {
    //     return ResponseEntity.ok(chatService.getChatMessagesByDate(roomId, date));
    // }

    // /**
    //  * 1대1 채팅 메시지 전송
    //  */
    // @MessageMapping("/chat/private/{roomId}")
    // public void sendPersonalChatMessage(
    //         @RequestParam("userId") final UUID userId,
    //         @PathVariable final UUID roomId,
    //         @Payload final MessageRequestDto request) {
    //     chatService.sendPersonalChatMessage(roomId, userId, request.content());
    // }

    // /**
    //  * 1대1 채팅방의 모든 대화 기록 삭제
    //  */
    // @DeleteMapping("/rooms/{roomId}/messages")
    // public ResponseEntity<Void> deleteAllPersonalChatMessages(
    //         @RequestParam final UUID userId,
    //         @PathVariable final UUID roomId) {
    //     chatService.deleteAllPersonalChatMessages(roomId, userId);
    //     return ResponseEntity.ok().build();
    // }

    // /**
    //  * 친구 요청 보내기
    //  */
    // @PostMapping("/friend-requests/{receiverId}")
    // public ResponseEntity<FriendRequest> sendFriendRequest(
    //         @RequestParam("userId") final UUID userId,
    //         @PathVariable final UUID receiverId) {
    //     chatService.sendFriendRequest(userId, receiverId);
    //     return ResponseEntity.ok().build();
    // }

    // /**
    //  * 친구 요청 수락
    //  */
    // @PostMapping("/friend-requests/{requestId}/accept")
    // public ResponseEntity<Friend> acceptFriendRequest(
    //         @PathVariable final Long requestId) {
    //     Friend friend = chatService.acceptFriendRequest(requestId);
    //     return ResponseEntity.ok().body(friend);
    // }

    // /**
    //  * 친구 요청 거절
    //  */
    // @PostMapping("/friend-requests/{requestId}/reject")
    // public ResponseEntity<Void> rejectFriendRequest(
    //         @PathVariable final Long requestId) {
    //     chatService.rejectFriendRequest(requestId);
    //     return ResponseEntity.ok().build();
    // }

    // /**
    //  * 닉네임으로 사용자 검색
    //  */
    // @GetMapping("/users/search")
    // public ResponseEntity<List<UserSearchResponseDto>> searchUsers(
    //         @RequestParam("userId") UUID userId,
    //         @RequestParam String nickname) {
    //     List<UserSearchResponseDto> users = chatService.searchUsersByNickname(userId, nickname);
    //     return ResponseEntity.ok(users);
    // }

    // /**
    //  * 받은 친구 요청 목록 조회
    //  */
    // @GetMapping("/friend-requests/received")
    // public ResponseEntity<List<FriendRequest>> getReceivedFriendRequests(
    //         @RequestParam("userId") final UUID userId) {
    //     List<FriendRequest> requests = chatService.getReceivedFriendRequests(userId);
    //     return ResponseEntity.ok().body(requests);
    // }
}
