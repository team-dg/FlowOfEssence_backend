package com.lolclone.chatdomain.exception;

public class MessageNotEditableException extends RuntimeException {
    private final Long messageId;

    public MessageNotEditableException(Long messageId) {
        super(String.format("메시지 수정이 불가능합니다. 메시지 ID: %s", messageId));
        this.messageId = messageId;
    }

    public Long getMessageId() {
        return messageId;
    }
}
