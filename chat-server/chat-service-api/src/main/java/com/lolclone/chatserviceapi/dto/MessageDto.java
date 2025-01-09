package com.lolclone.chatserviceapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lolclone.chatdomain.domain.Message;

public record MessageDto(
    Long messageId,
    UUID senderId,
    UUID receiverId,
    String message,
    LocalDateTime sentAt
) {
    public static MessageDto from(final Message message) {
        return new MessageDto(
            message.getId(),
            message.getSender().getId(),
            message.getRecipient().getId(),
            message.getContent(),
            message.getSentAt()
        );
    }
}
