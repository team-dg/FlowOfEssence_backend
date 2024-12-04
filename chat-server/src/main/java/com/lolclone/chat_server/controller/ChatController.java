package com.lolclone.chat_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import com.lolclone.chat_server.service.ChatService;

import jakarta.validation.Valid;

import com.lolclone.chat_server.dto.request.ChatHistoryRequest;
import com.lolclone.chat_server.dto.request.DeleteChatRequest;
import com.lolclone.chat_server.dto.request.MessageRequest;
import com.lolclone.chat_server.dto.response.MessageDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    
    private final ChatService chatService;
    
    @MessageMapping("/chat/private")
    public void handleMessage(@Valid @Payload final MessageRequest request) {
        chatService.sendMessage(request.userId(), request.receiverId(), request.message());
    }
    
    @GetMapping("/private/history")
    public ResponseEntity<List<MessageDto>> getChatHistory(
        @Valid @RequestBody final ChatHistoryRequest request
    ) {
        final List<MessageDto> chatHistory = chatService.getChatHistory(request.userId(), request.friendId());
        return ResponseEntity.ok().body(chatHistory);
    }
    
    @DeleteMapping("/private/history")
    public ResponseEntity<Void> deleteChat(
        @Valid @RequestBody final DeleteChatRequest request
    ) {
        chatService.deleteChat(request.userId(), request.friendId());
        return ResponseEntity.noContent().build();
    }
} 