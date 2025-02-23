package com.lolclone.authenticationmanagementinfra.service.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;
import com.lolclone.authenticationmanagementdomain.repository.UserAuthRepository;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.NotFoundException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.UnauthorizedException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementserviceapi.dto.SignUpRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;

    public Member getOrThrow(UUID id) {
        return userAuthRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }

    public Optional<Member> findMemberByUsername(String username) {
        return userAuthRepository.findByUsername(username);
    }

    public Optional<Member> findMemberByEmail(String email) {
        return userAuthRepository.findByEmail(email);
    }

    public Optional<Member> findBySocialName(String socialName) {
        return userAuthRepository.findBySocialName(socialName);
    }

    public void verifyPassword(Member member, String password) {
        if (!member.matchPassword(password, passwordEncoder)) {
            throw new UnauthorizedException(ExceptionType.INVALID_CREDENTIALS);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Member oAuth2MemberSave(Member member) {
        return userAuthRepository.save(member);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Member registerMember(SignUpRequest signUpRequest) {
        Member member = Member.builder()
            .username(signUpRequest.username())
            .password(signUpRequest.password())
            .email(signUpRequest.email())
            .nickname(signUpRequest.nickname())
            .socialName(OAuth2Provider.DEFAULT.getRegistrationId())
            .profileImageUrl(null)
            .build();

        findMemberByUsername(signUpRequest.username()).ifPresent(m -> {
            throw new UnauthorizedException(ExceptionType.DUPLICATED_USERNAME);
        });
        findMemberByEmail(signUpRequest.email()).ifPresent(m -> {
            throw new UnauthorizedException(ExceptionType.DUPLICATED_EMAIL);
        });

        String encodedPassword = passwordEncoder.encode(signUpRequest.password());
        member.setPassword(encodedPassword);

        Member savedMember = userAuthRepository.save(member);
        return savedMember;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteById(UUID id) {
        log.info("[DELETE MEMBER] userId: {}", id);
        userAuthRepository.deleteById(id);
    }
}
