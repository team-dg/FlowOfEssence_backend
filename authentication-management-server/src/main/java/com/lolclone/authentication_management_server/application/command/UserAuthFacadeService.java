package com.lolclone.authentication_management_server.application.command;

import java.util.UUID;

import com.lolclone.authentication_management_server.dto.*;
import com.lolclone.authentication_management_server.infrastructure.MemberAuthenticationTokenProvider;
import org.springframework.stereotype.Service;

import com.lolclone.authentication_management_server.application.OAuth2Client;
import com.lolclone.authentication_management_server.application.OAuth2Clients;
import com.lolclone.authentication_management_server.domain.OpenIdClient;
import com.lolclone.authentication_management_server.domain.OpenIdClients;
import com.lolclone.common_module.authenticationserver.domain.authentication.MemberAuthentication;
import com.lolclone.database_server.authenticationserver.domain.SocialType;
import com.lolclone.database_server.authenticationserver.domain.UserInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthFacadeService {
    private final OAuth2Clients oAuth2Clients;
    private final OpenIdClients openIdClients;
    private final UserAuthCommand userAuthCommand;
    private final MemberAuthenticationTokenProvider userAuthTokenProvider;

    public LoginResponse oAuth2Login(SocialType socialType, String code) {
        OAuth2Client oAuth2Client = oAuth2Clients.getClient(socialType);
        UserInfo userInfo = oAuth2Client.getUserInfo(code);
        return login(userInfo);
    }

    private LoginResponse login(UserInfo userInfo) {
        LoginResult loginResult = userAuthCommand.oauthLogin(userInfo);
        return createLoginResponse(loginResult);
    }

    public LoginResponse loginOriginal(LoginRequest userInfo) {
        LoginResult loginResult = userAuthCommand.login(userInfo);
        return createLoginResponse(loginResult);
    }

    public LoginResponse signUp(SignUpRequest signUpRequest) {
        LoginResult loginResult = userAuthCommand.signUp(signUpRequest);
        return createLoginResponse(loginResult);
    }

    private LoginResponse createLoginResponse(LoginResult loginResult) {
        TokenResponse accessToken = userAuthTokenProvider.provide(new MemberAuthentication(loginResult.userId()));
        return new LoginResponse(
                loginResult.nickname(),
                loginResult.profileImageUrl(),
                accessToken,
                new TokenResponse(
                        loginResult.refreshToken().toString(),
                        loginResult.refreshTokenExpiredAt()
                )
        );
    }

    public LoginResponse openIdLogin(SocialType socialType, String idToken) {
        OpenIdClient openIdClient = openIdClients.getClient(socialType);
        UserInfo userInfo = openIdClient.getUserInfo(idToken);
        return login(userInfo);
    }

    public void logout(Long userId, UUID refreshTokenId) {
        userAuthCommand.logout(userId, refreshTokenId);
    }

    public TokenRefreshResponse refresh(UUID refreshTokenId) {
        TokenRefreshResult tokenRefreshResult = userAuthCommand.refresh(refreshTokenId);
        Long userId = tokenRefreshResult.userId();
        TokenResponse accessToken = userAuthTokenProvider.provide(new MemberAuthentication(userId));
        return new TokenRefreshResponse(
            accessToken,
            new TokenResponse(
                tokenRefreshResult.token(),
                tokenRefreshResult.expiredAt()
            )
        );
    }

    public void deleteAccount(Long userId) {
        userAuthCommand.deleteAccount(userId);
    }
}
