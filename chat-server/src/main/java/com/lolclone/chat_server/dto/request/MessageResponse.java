package com.lolclone.chat_server.dto.request;

import java.util.UUID;

public record MessageResponse(
    UUID receiverId,
    String message
) {
    public static MessageResponse of(UUID receiverId, String message) {
        return new MessageResponse(receiverId, message);
    }
}
