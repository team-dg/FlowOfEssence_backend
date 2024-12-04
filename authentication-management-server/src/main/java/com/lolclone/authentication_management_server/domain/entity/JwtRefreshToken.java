package com.lolclone.authentication_management_server.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.lolclone.authentication_management_server.common.domain.BaseTimeEntity;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtRefreshToken extends BaseTimeEntity implements Persistable<UUID> {
    
    private static final long EXPIRED_OFFSET_DAY = 7;
    
    @Id
    private UUID id;

    private Long memberId;
    
    private LocalDateTime expiredAt;
    
    public JwtRefreshToken(final Long memberId, final LocalDateTime expiredAt) {
        this.id = UUID.randomUUID();
        this.memberId = memberId;
        this.expiredAt = expiredAt;
    }

    public static JwtRefreshToken of(Long memberId, LocalDateTime now) {
        return new JwtRefreshToken(memberId, now.plusDays(EXPIRED_OFFSET_DAY));
    }

    public boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }

    public boolean isOwner(Long memberId) {
        return Objects.equals(this.memberId, memberId);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
