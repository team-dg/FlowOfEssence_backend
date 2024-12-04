package com.lolclone.chat_server.dto.response;

import java.time.LocalDateTime;
import com.lolclone.chat_server.domain.Message;

public record MessageDto(
    Long messageId,
    Long senderId,
    Long receiverId,
    String message,
    LocalDateTime createdAt
) {
    public static MessageDto from(Message message) {
        return new MessageDto(
            message.getId(),
            message.getSenderId(),
            message.getReceiverId(),
            message.getMessage(),
            message.getCreatedDate()
        );
    }
} 