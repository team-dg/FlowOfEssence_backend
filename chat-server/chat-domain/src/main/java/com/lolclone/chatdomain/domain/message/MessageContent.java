package com.lolclone.chatdomain.domain.message;

import com.lolclone.chatdomain.exception.InvalidMessageContentException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageContent {
    private static final int MAX_LENGTH = 500;

    @Column(name = "content", nullable = false, length = MAX_LENGTH)
    private String content;

    private MessageContent(String content) {
        validateContent(content);
        this.content = content;
    }

    public static MessageContent of(String content) {
        return new MessageContent(content);
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidMessageContentException("메시지 내용은 비어있을 수 없습니다.");
        }
        if (content.length() > MAX_LENGTH) {
            throw new InvalidMessageContentException("메시지 내용이 너무 깁니다.");
        }
    }
}
