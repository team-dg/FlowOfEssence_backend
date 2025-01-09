package com.lolclone.chatinfra.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.Message;
import com.lolclone.chatinfra.service.application.ChatService;
import com.lolclone.chatserviceapi.dto.FriendChatInfoDto;
import com.lolclone.chatserviceapi.dto.MessageRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    /**
     * 1대1 채팅방 생성 또는 조회
     */
    @GetMapping("/rooms/personal/{friendId}")
    public ResponseEntity<ChatRoom> getOrCreatePersonalChatRoom(
        @RequestAttribute("userId") final UUID userId,
        @PathVariable final UUID friendId) {
        ChatRoom chatRoom = chatService.getOrCreatePersonalChatRoom(userId, friendId);
        return ResponseEntity.ok(chatRoom);
    }

    /**
     * 날짜별 채팅 메시지 조회
     */
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<Message>> getChatMessagesByDate(
            @PathVariable final UUID roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date) {
        List<Message> messages = chatService.getChatMessagesByDate(roomId, date);
        return ResponseEntity.ok(messages);
    }

    /**
     * 1대1 채팅 메시지 전송
     */
    @MessageMapping("/chat/private")
    public ResponseEntity<Message> sendPersonalChatMessage(
            @RequestAttribute("userId") final UUID userId,
            @PathVariable final UUID roomId,
            @RequestBody final MessageRequestDto request) {
        Message message = chatService.sendPersonalChatMessage(roomId, userId, request.content());
        return ResponseEntity.ok(message);
    }

    /**
     * 1대1 채팅방의 모든 대화 기록 삭제
     */
    @DeleteMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Void> deleteAllPersonalChatMessages(
            @RequestAttribute("userId") final UUID userId,
            @PathVariable final UUID roomId) {
        chatService.deleteAllPersonalChatMessages(roomId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 친구 목록 가나다순 조회 (채팅방 정보 포함)
     */
    @GetMapping("/friends")
    public ResponseEntity<List<FriendChatInfoDto>> getFriendListSortedByNickname(
            @RequestAttribute("userId") final UUID userId) {
        List<FriendChatInfoDto> friendChatInfos = chatService.getFriendListSortedByNickname(userId);
        return ResponseEntity.ok(friendChatInfos);
    }
}
