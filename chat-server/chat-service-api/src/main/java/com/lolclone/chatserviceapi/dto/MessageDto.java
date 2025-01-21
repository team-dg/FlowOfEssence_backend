package com.lolclone.chatserviceapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lolclone.chatdomain.domain.message.Message;

public record MessageDto(
    Long messageId,
    UUID senderId,
    String message,
    LocalDateTime sentAt
) {
    // public static MessageDto from(final Message message) {
    //     return new MessageDto(
    //         message.getId().getValue(),
    //         message.getSender().getId().getValue(),
    //         message.getContent().getValue(),
    //         message.getCreatedDate()
    //     );
    // }
}
