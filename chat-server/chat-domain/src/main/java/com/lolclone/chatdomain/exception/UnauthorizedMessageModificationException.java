package com.lolclone.chatdomain.exception;

import java.util.UUID;

public class UnauthorizedMessageModificationException extends RuntimeException {
    private final Long messageId;
    private final UUID userId;

    public UnauthorizedMessageModificationException(Long messageId, UUID userId) {
        super(String.format("메시지 수정이 권한이 없습니다. 메시지 ID: %s, 사용자 ID: %s", messageId, userId));
        this.messageId = messageId;
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Long getMessageId() {
        return messageId;
    }
}
