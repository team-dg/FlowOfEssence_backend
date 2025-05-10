package com.lolclone.authenticationmanagementinfra.service.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementdomain.domain.JwtRefreshToken;
import com.lolclone.authenticationmanagementdomain.repository.JwtRefreshTokenRepository;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.NotFoundException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
public class TokenManagementService {
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

    public JwtRefreshToken getOrElseThrow(UUID memberId) {
        return jwtRefreshTokenRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.JWT_REFRESH_TOKEN_NOT_FOUND));
    }

    public Optional<JwtRefreshToken> findTokenById(UUID memberId) {
        return jwtRefreshTokenRepository.findById(memberId);
    }

    public boolean isOwner(UUID memberId) {
        JwtRefreshToken jwtRefreshToken = getOrElseThrow(memberId);
        return jwtRefreshToken.isOwner(memberId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public JwtRefreshToken saveRefreshToken(UUID memberId, String refreshToken) {
        JwtRefreshToken jwtRefreshToken = JwtRefreshToken.builder()
            .memberId(memberId)
            .refreshToken(refreshToken)
            .build();
        return jwtRefreshTokenRepository.save(jwtRefreshToken);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteTokenById(UUID memberId) {
        jwtRefreshTokenRepository.deleteById(memberId);
    }
}
