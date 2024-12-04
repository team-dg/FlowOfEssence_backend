package com.lolclone.chat_server.controller;

import com.lolclone.chat_server.dto.event.UserStatusEvent;
import com.lolclone.chat_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserStatusController {
    
    private final UserService userService;
    
    @MessageMapping("/status/update")
    public void handleStatusUpdate(@Payload UserStatusEvent event) {
        userService.updateUserStatus(event);
    }
} 