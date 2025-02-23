package com.lolclone.authenticationmanagementinfra.service.application;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
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
import com.lolclone.authenticationmanagementinfra.exception.commonexception.UnauthorizedException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.service.TokenProviderTemplate;
import com.lolclone.authenticationmanagementinfra.service.domain.JwtTokenService;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;
import com.lolclone.authenticationmanagementinfra.service.domain.TokenManagementService;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginResult;
import com.lolclone.authenticationmanagementserviceapi.dto.SignUpRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenRefreshResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final OAuth2AuthorizedClientService oauth2AuthorizedClientService;
    private final OAuth2UserUnlinkService oauth2UserUnlinkService;
    private final TokenManagementService tokenManagementService;
    private final JwtTokenService jwtTokenService;
    private final TokenProviderTemplate tokenProviderTemplate;

    @Value("${refresh.reissue.threshold.minutes}")
    private long reissueThresholdMinutes;

    @Transactional
    public LoginResult originalLogin(LoginRequest loginRequest) {
        
        // 1. Spring Security 인증 처리
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        // 2. SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 인증된 사용자 정보 추출
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 4. 반환 -> 여기서 토큰 생성 아직 x
        return new LoginResult(userDetails.getMemberId(), userDetails.getEmail());
    }

    @Transactional
    public Member originalSignUp(SignUpRequest signUpRequest) {
        Member savedMember = memberService.registerMember(signUpRequest);
        return savedMember;
    }

    @Transactional
    public TokenRefreshResponse tokenCreate(Member member) {
        TokenRefreshResponse refreshTokenResponse = jwtTokenService.createTokenResponse(member, OAuth2Provider.DEFAULT);
        String refreshToken = refreshTokenResponse.refreshToken().token();
        tokenManagementService.saveRefreshToken(member.getId(), refreshToken);
        return refreshTokenResponse;
    }

    @Transactional
    public void logout(final OAuth2UserPrincipal oauth2UserPrincipal, final CustomUserDetails userDetails) {
        tokenManagementService.findTokenById(userDetails.getMemberId()).ifPresent(refreshToken -> {
            if(tokenManagementService.isOwner(userDetails.getMemberId())) {
                tokenManagementService.deleteTokenById(userDetails.getMemberId());
                if(oauth2UserPrincipal instanceof OAuth2UserPrincipal) {
                    oauth2AuthorizedClientService.removeAuthorizedClient(
                        oauth2UserPrincipal.getRegistrationId(),
                        oauth2UserPrincipal.getName()
                    );
                }
            }
        });
    }

    @Transactional
    public TokenRefreshResponse refreshToken(final String refreshToken, final OAuth2Provider provider) {
        // 1. RefreshToken 검증
        final TokenClaims tokenClaims = jwtTokenService.verifyAndGetClaims(refreshToken);

        // 2. RefreshToken에서 사용자 정보 추출
        UUID memberId = tokenClaims.getId();

        // 3. 사용자 정보 조회
        Member member = memberService.getOrThrow(memberId);

        // 4. DB에 저장된 RefreshToken과 비교
        String savedRefreshToken = tokenManagementService.getOrElseThrow(memberId).getRefreshToken();

        // 5. RefreshToken이 일치하지 않으면 예외 발생
        if (!refreshToken.equals(savedRefreshToken))
            throw new UnauthorizedException(ExceptionType.INVALID_CREDENTIALS);

        // 6. RefreshToken 만료 시간 확인
        long remainingTime = tokenProviderTemplate.getRemainTime(refreshToken);
        
        // 7. RefreshToken 재발급 여부 결정 (예: 1일 이내로 남은 경우)
        return reissueRefreshToken(remainingTime) 
            ? jwtTokenService.createTokenResponse(member, provider)
            : jwtTokenService.createAccessTokenResponse(member, provider, refreshToken);
    }

    private boolean reissueRefreshToken(Long remainingTime) {
        return remainingTime < TimeUnit.MINUTES.toMillis(reissueThresholdMinutes);
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
