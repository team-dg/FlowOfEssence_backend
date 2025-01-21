package com.lolclone.chatdomain.domain.member;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberId implements Serializable {
    @Column(name = "user_id", columnDefinition = "uuid")
    private UUID value;

    private MemberId(UUID value) {
        this.value = value;
    }

    public static MemberId of(UUID value) {
        return new MemberId(value);
    }

    public UUID getValue() {
        return value;
    }
}
