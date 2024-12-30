package com.lolclone.authenticationmanagementinfra.service.domain;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementdomain.domain.JwtRefreshToken;
import com.lolclone.authenticationmanagementdomain.repository.JwtRefreshTokenRepository;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.UnauthorizedException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
public class TokenManagementService {
    private final Clock clock;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

    public Optional<JwtRefreshToken> findTokenById(UUID refreshTokenId) {
        return jwtRefreshTokenRepository.findById(refreshTokenId);
    }

    public JwtRefreshToken validateRefreshToken(UUID refreshTokenId) {
        return findTokenById(refreshTokenId).orElseThrow(() -> {
            log.warn("탈취 가능성이 있는 리프레쉬 토큰이 존재합니다. token={}", refreshTokenId);
            throw new UnauthorizedException(ExceptionType.INVALID_REFRESH_TOKEN);
        });
    }

    public boolean isExpired(JwtRefreshToken refreshToken, LocalDateTime now) {
        return refreshToken.isExpired(now);
    }

    public boolean isOwner(UUID userId, UUID refreshTokenId) {
        return findTokenById(refreshTokenId)
            .map(token -> token.isOwner(userId))
            .orElse(false);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public JwtRefreshToken saveRefreshToken(UUID userId) {
        return jwtRefreshTokenRepository.save(JwtRefreshToken.of(userId, LocalDateTime.now(clock)));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteTokenById(UUID refreshTokenId) {
        jwtRefreshTokenRepository.deleteById(refreshTokenId);
    }
}
