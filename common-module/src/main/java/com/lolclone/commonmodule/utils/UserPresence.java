package com.lolclone.commonmodule.utils;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lolclone.commonmodule.domain.MemberStatus;

/**
 * 사용자의 현재 프레즌스(상태) 정보를 나타내는 불변 데이터 구조 (Record 사용).
 * 주로 Redis와 같은 Key-Value 저장소에 저장됩니다. (Key: userId, Value: UserPresence 객체 직렬화된 형태)
 */
public record UserPresence(
    /**
     * 사용자 고유 ID (필수)
     */
    UUID userId,

    /**
     * 현재 상태 (필수)
     */
    MemberStatus status,

    /**
     * 상태가 마지막으로 업데이트된 시간 (필수)
     */
    LocalDateTime lastUpdatedAt
) {
    
}
