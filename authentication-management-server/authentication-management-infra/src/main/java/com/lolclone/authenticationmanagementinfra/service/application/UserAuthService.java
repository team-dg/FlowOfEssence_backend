package com.lolclone.authenticationmanagementinfra.service.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementdomain.domain.JwtRefreshToken;
import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.UnauthorizedException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.sagaorchestrator.saga.SignUpSagaState;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;
import com.lolclone.authenticationmanagementinfra.service.domain.TokenManagementService;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginResult;
import com.lolclone.authenticationmanagementserviceapi.dto.SignUpRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenRefreshResult;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.UserInfo;

import io.eventuate.tram.sagas.orchestration.SagaManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {
    private final MemberService memberService;
    private final TokenManagementService tokenManagementService;
    private final Clock clock;
    private final SagaManager<SignUpSagaState> signUpSagaManager;

    @Transactional(propagation = Propagation.MANDATORY)
    public Member oAuth2Login(UserInfo userInfo) {
        Member savedMember = memberService.findMemberBySocialInfo(userInfo).orElseGet(() -> memberService.registerSocialMember(userInfo));
        SignUpSagaState data = new SignUpSagaState(savedMember.getId(), savedMember.getNickname());
        signUpSagaManager.create(data, Member.class, savedMember.getId());
        return savedMember;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public LoginResult originalLogin(LoginRequest loginRequest) {
        Member member = memberService.findMemberByUsername(loginRequest.username()).orElseThrow(() -> new UnauthorizedException(ExceptionType.INVALID_CREDENTIALS));
        memberService.verifyPassword(member, loginRequest.password());
        JwtRefreshToken jwtRefreshToken = tokenManagementService.saveRefreshToken(member.getId());
        return new LoginResult(member.getId(), member.getNickname(), jwtRefreshToken.getId(), jwtRefreshToken.getExpiredAt());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Member originalSignUp(SignUpRequest signUpRequest) {
        Member savedMember = memberService.registerMember(signUpRequest);
        SignUpSagaState data = new SignUpSagaState(savedMember.getId(), savedMember.getNickname());
        signUpSagaManager.create(data, Member.class, savedMember.getId());
        return savedMember;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public LoginResult TokenCreate(UUID userId) {
        Member member = memberService.getOrThrow(userId);
        JwtRefreshToken jwtRefreshToken = tokenManagementService.saveRefreshToken(userId);
        return new LoginResult(userId, member.getNickname(), jwtRefreshToken.getId(), jwtRefreshToken.getExpiredAt());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void logout(UUID userId, UUID refreshTokenId) {
        tokenManagementService.findTokenById(refreshTokenId).ifPresent(refreshToken -> {
            if(tokenManagementService.isOwner(userId, refreshTokenId)) {
                tokenManagementService.deleteTokenById(refreshTokenId);
            }
        });
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public TokenRefreshResult refresh(UUID refreshTokenId) {
        JwtRefreshToken jwtRefreshToken = tokenManagementService.validateRefreshToken(refreshTokenId);
        if(tokenManagementService.isExpired(jwtRefreshToken, LocalDateTime.now(clock))) {
            log.info("만료된 리프레쉬 토큰이 있습니다. memberId={}, token={}", jwtRefreshToken.getMemberId(), refreshTokenId);
            throw new UnauthorizedException(ExceptionType.EXPIRED_REFRESH_TOKEN);
        }
        tokenManagementService.deleteTokenById(refreshTokenId);
        JwtRefreshToken newRefreshToken = tokenManagementService.saveRefreshToken(jwtRefreshToken.getMemberId());
        return new TokenRefreshResult(newRefreshToken.getMemberId(), newRefreshToken.getId(), newRefreshToken.getExpiredAt());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAccount(UUID userId) {
        Member member = memberService.getOrThrow(userId);
        memberService.deleteMember(member);
    }
}
