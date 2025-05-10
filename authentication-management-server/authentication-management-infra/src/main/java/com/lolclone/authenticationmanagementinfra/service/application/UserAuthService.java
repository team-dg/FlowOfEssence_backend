package com.lolclone.authenticationmanagementinfra.service.application;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementdomain.domain.CustomUserDetails;
import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementdomain.domain.TokenClaims;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2UserPrincipal;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.NotFoundException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.UnauthorizedException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.service.TokenProviderTemplate;
import com.lolclone.authenticationmanagementinfra.service.domain.JwtTokenService;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;
import com.lolclone.authenticationmanagementinfra.service.domain.TokenManagementService;
import com.lolclone.authenticationmanagementinfra.utils.RedisUtils;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.SignUpRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenRefreshResponse;
import com.lolclone.commonmodule.domain.SessionStatus;
import com.lolclone.commonmodule.domain.UserSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {
    private final MemberService memberService;
    private final OAuth2AuthorizedClientService oauth2AuthorizedClientService;
    private final OAuth2UserUnlinkService oauth2UserUnlinkService;
    private final TokenManagementService tokenManagementService;
    private final JwtTokenService jwtTokenService;
    private final TokenProviderTemplate tokenProviderTemplate;
    private final RedisUtils redisUtils;

    @Value("${refresh.reissue.threshold.minutes}")
    private long reissueThresholdMinutes;

    @Transactional
    public TokenRefreshResponse originalLogin(LoginRequest loginRequest) {
        // 1. 사용자 정보 조회
        Member member = memberService.findMemberByUsername(loginRequest.username())
                .orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));

        // 2. 비밀번호 검증
        memberService.verifyPassword(member, loginRequest.password());
        
        // 3. 인증 성공 처리
        CustomUserDetails userDetails = CustomUserDetails.of(OAuth2Provider.DEFAULT, member);

        // 4. 인증 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 5. SecurityContextHolder에 인증 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 6. 반환
        TokenRefreshResponse tokenResponse = createSession(member.getId(), member);

        return tokenResponse;
    }

    @Transactional
    public TokenRefreshResponse createSession(UUID memberId, Member member) {
        UserSession existingSession = redisUtils.getUserSession(memberId.toString());
        if(existingSession != null) {
            redisUtils.invalidateUserSession(memberId.toString());
        }

        TokenRefreshResponse refreshTokenResponse = jwtTokenService.createTokenResponse(member, OAuth2Provider.DEFAULT);
        
        saveUserSession(memberId, refreshTokenResponse);

        return refreshTokenResponse;
    }

    private void saveUserSession(UUID memberId, TokenRefreshResponse refreshTokenResponse) {
        String accessToken = refreshTokenResponse.accessToken().token();
        String refreshToken = refreshTokenResponse.refreshToken().token();

        UserSession userSession = UserSession.createNew(memberId, accessToken, refreshToken);

        Long refreshTokenTtl = tokenProviderTemplate.getRemainTime(refreshToken);
        redisUtils.saveUserSession(userSession, refreshTokenTtl);
    }

    @Transactional
    public Member originalSignUp(SignUpRequest signUpRequest) {
        Member savedMember = memberService.registerMember(signUpRequest);
        return savedMember;
    }

    @Transactional
    public void cancelSignUp(UUID memberId) {
        memberService.deleteById(memberId);
    }

    @Transactional
    public void logout(final OAuth2UserPrincipal oauth2UserPrincipal, final CustomUserDetails userDetails) {
        UUID memberId = userDetails.getMemberId();

        // 1. 사용자 세션 상태를 REVOKED로 변경
        UserSession userSession = redisUtils.getUserSession(memberId.toString());

        if (userSession != null && userSession.getStatus() == SessionStatus.ACTIVE) {
            // 2. 세션 상태를 REVOKED로 변경
            redisUtils.updateSessionStatus(memberId, SessionStatus.REVOKED);
            
            // 3. 액세스 토큰을 블랙리스트에 추가
            String accessToken = userSession.getAccessToken();
            long accessTokenRemainTime = tokenProviderTemplate.getRemainTime(accessToken);
            redisUtils.setBlacklistToken(accessToken, memberId, accessTokenRemainTime);
            
            // 4. 리프레시 토큰을 블랙리스트에 추가
            String refreshToken = userSession.getRefreshToken();
            long refreshTokenRemainTime = tokenProviderTemplate.getRemainTime(refreshToken);
            redisUtils.setBlacklistToken(refreshToken, memberId, refreshTokenRemainTime);
        }
        
        // 5. OAuth2 클라이언트 제거
        if(oauth2UserPrincipal instanceof OAuth2UserPrincipal) {
            oauth2AuthorizedClientService.removeAuthorizedClient(
                oauth2UserPrincipal.getRegistrationId(),
                oauth2UserPrincipal.getName()
            );
        }
    }

    @Transactional
    public TokenRefreshResponse refreshToken(final String refreshToken, final OAuth2Provider provider) {
        // 1. RefreshToken 검증
        final TokenClaims tokenClaims = jwtTokenService.verifyAndGetClaims(refreshToken);

        // 2. RefreshToken에서 사용자 정보 추출
        UUID memberId = tokenClaims.getId();

        // 3. 사용자 정보 조회
        Member member = memberService.getOrThrow(memberId);

        // 4. Redis에서 사용자 세션 조회
        UserSession userSession = redisUtils.getUserSession(memberId.toString());
        
        // 5. 세션이 없거나 ACTIVE 상태가 아니면 예외 발생
        if (userSession == null)
            throw new UnauthorizedException(ExceptionType.USER_SESSION_NOT_FOUND);

        if (userSession.getStatus() != SessionStatus.ACTIVE)
            throw new UnauthorizedException(ExceptionType.USER_SESSION_EXPIRED);

        // 6. 세션의 리프레시 토큰과 요청된 리프레시 토큰 비교
        if (!refreshToken.equals(userSession.getRefreshToken()))
            throw new UnauthorizedException(ExceptionType.INVALID_CREDENTIALS);

        // 7. RefreshToken 만료 시간 확인
        long remainingTime = tokenProviderTemplate.getRemainTime(refreshToken);
        
        // 8. RefreshToken 재발급 여부 결정 (예: 1일 이내로 남은 경우)
        TokenRefreshResponse tokenResponse = reissueRefreshToken(remainingTime)
            ? jwtTokenService.createTokenResponse(member, provider)
            : jwtTokenService.createAccessTokenResponse(member, provider, refreshToken);
        
        // 9. 세션 정보 업데이트
        updateUserSession(userSession, tokenResponse, remainingTime);
        
        return tokenResponse;
    }

    private boolean reissueRefreshToken(Long remainingTime) {
        return remainingTime < TimeUnit.MINUTES.toMillis(reissueThresholdMinutes);
    }

    private void updateUserSession(UserSession userSession, TokenRefreshResponse tokenResponse, long remainingTime) {
        String newAccessToken = tokenResponse.accessToken().token();
        String newRefreshToken = tokenResponse.refreshToken().token();
        
        // 액세스 토큰 업데이트
        userSession.updateAccessToken(newAccessToken);

        // 리프레시 토큰이 재발급된 경우 업데이트
        if (tokenResponse.refreshToken() != null && !userSession.getRefreshToken().equals(newRefreshToken)) {
            userSession.updateRefreshToken(newRefreshToken);
        }

        // 마지막 접근 시간 업데이트
        userSession.updateLastAccessTime();

        long ttl = newRefreshToken != null ? tokenProviderTemplate.getRemainTime(newRefreshToken) : remainingTime;

        redisUtils.saveUserSession(userSession, ttl);
    }

    @Transactional
    public void deleteAccount(final UUID memberId) {
        memberService.deleteById(memberId);
    }

    @Transactional
    public void unlink(final OAuth2Provider provider, final OAuth2UserPrincipal oauth2UserPrincipal) {
        final OAuth2AuthorizedClient client = oauth2AuthorizedClientService.loadAuthorizedClient(
            oauth2UserPrincipal.getRegistrationId(),
            oauth2UserPrincipal.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        // 소셜 로그인 연결 해제
        oauth2UserUnlinkService.unlink(provider, oauth2UserPrincipal.getId(), accessToken);
        // 회원 삭제
        memberService.deleteById(oauth2UserPrincipal.getId());
        // 토큰 삭제
        tokenManagementService.deleteTokenById(oauth2UserPrincipal.getId());
        // 인증 클라이언트 삭제
        oauth2AuthorizedClientService.removeAuthorizedClient(
            oauth2UserPrincipal.getRegistrationId(),
            oauth2UserPrincipal.getName()
        );
    }
}
