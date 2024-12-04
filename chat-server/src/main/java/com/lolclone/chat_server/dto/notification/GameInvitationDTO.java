package com.lolclone.chat_server.dto.notification;

import java.time.LocalDateTime;

public record GameInvitationDTO(
    Long senderId,
    Long roomId,
    String message,
    LocalDateTime timestamp
) {
    public static GameInvitationDTO of(Long senderId, Long roomId, String message) {
        return new GameInvitationDTO(
            senderId,
            roomId,
            message,
            LocalDateTime.now()
        );
    }
} 