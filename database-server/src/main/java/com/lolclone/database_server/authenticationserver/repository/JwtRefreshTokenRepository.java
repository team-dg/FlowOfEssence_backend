package com.lolclone.database_server.authenticationserver.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.database_server.authenticationserver.domain.JwtRefreshToken;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, UUID> {
    JwtRefreshToken save(JwtRefreshToken jwtRefreshToken);

    Optional<JwtRefreshToken> findById(UUID id);

    void deleteById(UUID id);
}
