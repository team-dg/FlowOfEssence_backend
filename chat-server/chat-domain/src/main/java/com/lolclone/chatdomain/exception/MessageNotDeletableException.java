package com.lolclone.chatdomain.exception;

public class MessageNotDeletableException extends RuntimeException {
    private final Long messageId;

    public MessageNotDeletableException(Long messageId) {
        super(String.format("메시지 삭제가 불가능합니다. 메시지 ID: %s", messageId));
        this.messageId = messageId;
    }

    public Long getMessageId() {
        return messageId;
    }
}
