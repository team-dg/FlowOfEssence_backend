package com.lolclone.chat_server.dto.notification;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameInvitationDTO(
    UUID senderId,
    UUID roomId,
    String message,
    LocalDateTime timestamp
) {
    public static GameInvitationDTO of(UUID senderId, UUID roomId, String message) {
        return new GameInvitationDTO(
            senderId,
            roomId,
            message,
            LocalDateTime.now()
        );
    }
} 