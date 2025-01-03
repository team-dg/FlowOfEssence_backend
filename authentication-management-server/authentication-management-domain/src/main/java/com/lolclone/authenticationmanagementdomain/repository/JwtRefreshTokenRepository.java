package com.lolclone.authenticationmanagementdomain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.authenticationmanagementdomain.domain.JwtRefreshToken;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, UUID> {
    Optional<JwtRefreshToken> findById(UUID id);

    void deleteById(UUID id);
}
