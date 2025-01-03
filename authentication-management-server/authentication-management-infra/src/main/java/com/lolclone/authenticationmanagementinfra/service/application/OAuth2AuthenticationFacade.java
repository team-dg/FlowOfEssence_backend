package com.lolclone.authenticationmanagementinfra.service.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Client;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Clients;
import com.lolclone.authenticationmanagementdomain.domain.openid.OpenIdClient;
import com.lolclone.authenticationmanagementdomain.domain.openid.OpenIdClients;
import com.lolclone.authenticationmanagementinfra.token.MemberAuthenticationTokenProvider;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginResponse;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginResult;
import com.lolclone.authenticationmanagementserviceapi.dto.SignUpRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenRefreshResponse;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenRefreshResult;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenResponse;
import com.lolclone.commonmodule.apigatewayserver.domain.MemberAuthentication;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.UserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuth2AuthenticationFacade {
    private final OAuth2Clients oAuth2Clients;
    private final OpenIdClients openIdClients;
    private final UserAuthService userAuthService;
    private final MemberAuthenticationTokenProvider memberAuthenticationTokenProvider;
    
    @Transactional
    public LoginResponse oAuth2Login(SocialType socialType, String code) {
        OAuth2Client oAuth2Client = oAuth2Clients.getClient(socialType);
        UserInfo userInfo = oAuth2Client.getUserInfo(code);
        return login(userInfo);
    }

    @Transactional
    public LoginResponse openIdLogin(SocialType socialType, String idToken) {
        OpenIdClient openIdClient = openIdClients.getClient(socialType);
        UserInfo userInfo = openIdClient.getUserInfo(idToken);
        return login(userInfo);
    }

    @Transactional
    public LoginResponse login(UserInfo userInfo) {
        LoginResult loginResult = userAuthService.oAuth2Login(userInfo);
        return createLoginResponse(loginResult);
    }

    @Transactional
    public LoginResponse originalLogin(LoginRequest request) {
        LoginResult loginResult = userAuthService.originalLogin(request);
        return createLoginResponse(loginResult);
    }

    @Transactional
    public LoginResponse originalSignUp(SignUpRequest signUpRequest) {
        LoginResult loginResult = userAuthService.originalSignUp(signUpRequest);
        return createLoginResponse(loginResult);
    }

    private LoginResponse createLoginResponse(LoginResult loginResult) {
        TokenResponse accessToken = memberAuthenticationTokenProvider.provide(new MemberAuthentication(loginResult.userId()));
        return new LoginResponse(
                accessToken,
                new TokenResponse(
                        loginResult.refreshToken().toString(),
                        loginResult.refreshTokenExpiredAt()
                ),
                loginResult.userId()
        );
    }

    @Transactional
    public void logOut(UUID userId, UUID refreshTokenId) {
        userAuthService.logout(userId, refreshTokenId);
    }

    @Transactional
    public TokenRefreshResponse refresh(UUID refreshTokenId) {
        TokenRefreshResult tokenRefreshResult = userAuthService.refresh(refreshTokenId);
        TokenResponse accessToken = memberAuthenticationTokenProvider.provide(new MemberAuthentication(tokenRefreshResult.userId()));
        return new TokenRefreshResponse(
            accessToken,
            new TokenResponse(
                tokenRefreshResult.refreshToken().toString(),
                tokenRefreshResult.expiredAt()
            )
        );
    }

    @Transactional
    public void deleteAccount(UUID userId) {
        userAuthService.deleteAccount(userId);
    }
}
