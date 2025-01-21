package com.lolclone.chatdomain.domain.chatparticipant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo {
    private static final int MAX_LENGTH = 255;

    @Column(name = "memo")
    private String value;

    private Memo(String value) {
        validate(value);
        this.value = value;
    }

    public static Memo of(String value) {
        return new Memo(value);
    }

    public static Memo empty() {
        return new Memo("");
    }

    private void validate(String value) {
        if (value == null) {
            throw new IllegalArgumentException("메모 내용은 null일 수 없습니다.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("메모 내용은 " + MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
    }
}
