package com.lolclone.authentication_management_server.presentation;

import java.util.UUID;

import com.lolclone.authentication_management_server.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lolclone.common_module.authenticationserver.annotation.UserAuth;
import com.lolclone.authentication_management_server.application.command.UserAuthFacadeService;
import com.lolclone.common_module.authenticationserver.domain.authentication.MemberAuthentication;
import com.lolclone.database_server.authenticationserver.domain.SocialType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private final UserAuthFacadeService userAuthFacadeService;

    @PostMapping("/login/oauth2")
    public ResponseEntity<LoginResponse> oauth2Login(
        @Valid @RequestBody final OAuth2LoginRequest oauth2LoginRequest
    ) {
        final LoginResponse loginResponse = userAuthFacadeService.oAuth2Login(oauth2LoginRequest.socialType(), oauth2LoginRequest.code());
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody final LoginRequest loginRequest
    ) {
        final LoginResponse loginResponse = userAuthFacadeService.loginOriginal(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signUp(
        @Valid @RequestBody final SignUpRequest signUpRequest
    ) {
        final LoginResponse loginResponse = userAuthFacadeService.signUp(signUpRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping("/login/oauth2/{socialType}")
    public ResponseEntity<LoginResponse> oauth2LoginRedirect(
        @PathVariable final SocialType socialType,
        @RequestParam final String code
    ) {
        final LoginResponse loginResponse = userAuthFacadeService.oAuth2Login(socialType, code);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/login/open-id")
    public ResponseEntity<LoginResponse> openIdLogin(
        @Valid @RequestBody final OpenIdLoginRequest openIdLoginRequest
    ) {
        final LoginResponse loginResponse = userAuthFacadeService.openIdLogin(openIdLoginRequest.socialType(), openIdLoginRequest.idToken());
        return ResponseEntity.ok().body(loginResponse);
    }

    @UserAuth
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        final MemberAuthentication memberAuthentication,
        @RequestBody @Valid final LogoutRequest logoutRequest
    ) {
        userAuthFacadeService.logout(memberAuthentication.getId(), UUID.fromString(logoutRequest.refreshToken()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(
        @RequestBody @Valid final RefreshTokenRequest refreshTokenRequest
    ) {
        final TokenRefreshResponse tokenRefreshResponse = userAuthFacadeService.refresh(UUID.fromString(refreshTokenRequest.refreshToken()));
        return ResponseEntity.ok().body(tokenRefreshResponse);
    }
    
    @UserAuth
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(final MemberAuthentication memberAuthentication) {
        userAuthFacadeService.deleteAccount(memberAuthentication.getId());
        return ResponseEntity.ok().build();
    }
}
