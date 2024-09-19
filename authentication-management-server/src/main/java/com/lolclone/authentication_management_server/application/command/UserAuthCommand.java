package com.lolclone.authentication_management_server.application.command;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import com.lolclone.authentication_management_server.dto.LoginRequest;
import com.lolclone.authentication_management_server.dto.SignUpRequest;
import com.lolclone.database_server.authenticationserver.domain.Member;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authentication_management_server.dto.LoginResult;
import com.lolclone.authentication_management_server.dto.TokenRefreshResult;
import com.lolclone.authentication_management_server.event.UserCreatedEvent;
import com.lolclone.authentication_management_server.event.UserDeletedEvent;
import com.lolclone.common_module.commonexception.UnauthorizedException;
import com.lolclone.common_module.exception.domain.ExceptionType;
import com.lolclone.database_server.authenticationserver.domain.JwtRefreshToken;
import com.lolclone.database_server.authenticationserver.domain.UserInfo;
import com.lolclone.database_server.authenticationserver.repository.JwtRefreshTokenRepository;
import com.lolclone.database_server.authenticationserver.repository.UserAuthRepository;
import com.lolclone.authentication_management_server.domain.UserInfoMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthCommand {
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final UserAuthRepository userAuthRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserInfoMapper userInfoMapper;
    private final Clock clock;
    private final PasswordEncoder passwordEncoder;

    public LoginResult oauthLogin(UserInfo userInfo) {
        Member member = userAuthRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType())
            .orElseGet(() -> oauthSignUp(userInfo));

        JwtRefreshToken jwtRefreshToken = saveRefreshToken(member.getId());
        return new LoginResult(
            member.getId(),
            member.getNickname(),
            member.getProfileImage(),
            jwtRefreshToken.getId(),
            jwtRefreshToken.getExpiredAt()
        );
    }

    public LoginResult login(LoginRequest loginRequest) {
        Member member = userAuthRepository.findByUsername(loginRequest.username())
            .orElseThrow(() -> new UnauthorizedException(ExceptionType.INVALID_CREDENTIALS));

        if(!member.matchPassword(loginRequest.password(), passwordEncoder)) {
            throw new UnauthorizedException(ExceptionType.INVALID_CREDENTIALS);
        }

        JwtRefreshToken jwtRefreshToken = saveRefreshToken(member.getId());

        return new LoginResult(
                member.getId(),
                member.getNickname(),
                null,
                jwtRefreshToken.getId(),
                jwtRefreshToken.getExpiredAt()
        );
    }

    private Member oauthSignUp(UserInfo userInfo) {
        Member member = userInfoMapper.toUser(userInfo);
        applicationEventPublisher.publishEvent(new UserCreatedEvent(member));
        return userAuthRepository.save(member);
    }

    public LoginResult signUp(SignUpRequest signUpRequest) {
        SignUpRequest.from(signUpRequest);

        if(userAuthRepository.existsByUsername(signUpRequest.username())) {
            throw new UnauthorizedException(ExceptionType.DUPLICATED_USERNAME);
        }

        if(userAuthRepository.existsByEmail(signUpRequest.email())) {
            throw new UnauthorizedException(ExceptionType.DUPLICATED_EMAIL);
        }

        if (!isValidPassword(signUpRequest.password())) {
            throw new UnauthorizedException(ExceptionType.INVALID_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.password());

        Member member = Member.of(signUpRequest.username(), encodedPassword, signUpRequest.nickname(), signUpRequest.email());

        // 회원 저장
        Member savedMember = userAuthRepository.save(member);

        // 이벤트 발행
        applicationEventPublisher.publishEvent(new UserCreatedEvent(savedMember));

        // 리프레시 토큰 생성
        JwtRefreshToken jwtRefreshToken = saveRefreshToken(savedMember.getId());

        // 로그인 결과 반환
        return new LoginResult(
            savedMember.getId(),
            savedMember.getNickname(),
            null,
            jwtRefreshToken.getId(),
            jwtRefreshToken.getExpiredAt()
        );
    }

    private boolean isValidPassword(String password) {
        // 대문자, 소문자, 숫자, 특수 문자가 포함되었는지 확인하는 로직
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    private JwtRefreshToken saveRefreshToken(Long userId) {
        return jwtRefreshTokenRepository.save(JwtRefreshToken.of(userId, LocalDateTime.now(clock)));
    }
    
    public void logout(Long userId, UUID refreshTokenId) {
        jwtRefreshTokenRepository.findById(refreshTokenId)
            .ifPresent(refreshToken -> {
                if (refreshToken.isOwner(userId)) {
                    jwtRefreshTokenRepository.deleteById(refreshToken.getId());
                }
            });
    }

    public TokenRefreshResult refresh(UUID refreshTokenId) {
        JwtRefreshToken refreshToken = jwtRefreshTokenRepository.findById(refreshTokenId)
            .orElseThrow(() -> {
                log.warn("탈취 가능성이 있는 리프레쉬 토큰이 존재합니다. token={}", refreshTokenId);
                return new UnauthorizedException(ExceptionType.INVALID_REFRESH_TOKEN);
            });
        if (refreshToken.isExpired(LocalDateTime.now(clock))) {
            log.info("만료된 리프레쉬 토큰이 있습니다. memberId={}, token={}", refreshToken.getMemberId(), refreshTokenId);
            throw new UnauthorizedException(ExceptionType.EXPIRED_REFRESH_TOKEN);
        }
        jwtRefreshTokenRepository.deleteById(refreshTokenId);
        JwtRefreshToken newRefreshToken = saveRefreshToken(refreshToken.getMemberId());
        return new TokenRefreshResult(
            newRefreshToken.getMemberId(),
            newRefreshToken.getId().toString(),
            newRefreshToken.getExpiredAt()
        );
    }

    public void deleteAccount(Long userId) {
        Member member = userAuthRepository.getOrThrow(userId);
        logDeleteUser(member);
        userAuthRepository.delete(member);
        applicationEventPublisher.publishEvent(new UserDeletedEvent(member));
    }

    private void logDeleteUser(Member member) {
        log.info("[DELETE MEMBER] userId: {} / socialType: {} / socialId: {}",
            member.getId(), member.getSocialType(), member.getSocialId());
    }
}
