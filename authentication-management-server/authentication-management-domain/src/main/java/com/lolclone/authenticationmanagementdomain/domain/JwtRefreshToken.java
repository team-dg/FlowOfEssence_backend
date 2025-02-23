package com.lolclone.authenticationmanagementdomain.domain;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Persistable;

import com.lolclone.authenticationmanagementdomain.common.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 우선은 db에 저장하는 로직으로 수정 하자.
 * TODO : 추후에 redis에 저장하는 로직으로 수정 필요
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtRefreshToken extends BaseTimeEntity implements Persistable<UUID> {
    @Id
    private UUID memberId;
    
    private String refreshToken;
    
    @Builder
    public JwtRefreshToken(final UUID memberId, final String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    @Override
    public UUID getId() {
        return memberId;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public boolean isOwner(UUID memberId) {
        return Objects.equals(this.memberId, memberId);
    }
}
