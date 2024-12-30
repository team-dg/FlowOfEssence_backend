package com.lolclone.authenticationmanagementinfra.service.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementdomain.domain.MemberDomainEvent;
import com.lolclone.authenticationmanagementdomain.repository.UserAuthRepository;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.MemberNotFoundException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.NotFoundException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.UnauthorizedException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.sagaorchestrator.producer.AuthenticationDomainEventPublisher;
import com.lolclone.authenticationmanagementserviceapi.dto.SignUpRequest;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.UserInfo;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationDomainEventPublisher memberAggregateEventPublisher;
    
    public Optional<Member> findMemberBySocialInfo(UserInfo userInfo) {
        return userAuthRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType());
    }

    public Optional<Member> findMemberByUsername(String username) {
        return userAuthRepository.findByUsername(username);
    }

    public Optional<Member> findMemberByEmail(String email) {
        return userAuthRepository.findByEmail(email);
    }

    public void verifyPassword(Member member, String password) {
        if (!member.matchPassword(password, passwordEncoder)) {
            throw new UnauthorizedException(ExceptionType.INVALID_CREDENTIALS);
        }
    }

    public Member getOrThrow(UUID id) {
        return userAuthRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }

    private Member updateMember(UUID userId, Function<Member, List<MemberDomainEvent>> updater) {
        return userAuthRepository.findById(userId).map(member -> {
            memberAggregateEventPublisher.publish(member, updater.apply(member));
            return member;
        }).orElseThrow(() -> new MemberNotFoundException(ExceptionType.USER_NOT_FOUND, userId.toString()));
    }

    public void createSignUpUser(UUID userId) {
        updateMember(userId, Member::noteUserCreated);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Member registerSocialMember(UserInfo userInfo) {
        ResultWithDomainEvents<Member, MemberDomainEvent> memberAndEvents = Member.createSocialUser(userInfo);
        Member member = memberAndEvents.result;
        Member savedMember = userAuthRepository.save(member);
        return savedMember;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Member registerMember(SignUpRequest signUpRequest) {
        ResultWithDomainEvents<Member, MemberDomainEvent> memberAndEvents = Member.createUser(signUpRequest.username(),signUpRequest.password(), signUpRequest.email(), signUpRequest.nickname());
        Member member = memberAndEvents.result;
        findMemberByUsername(member.getUsername()).ifPresent(m -> { throw new UnauthorizedException(ExceptionType.DUPLICATED_USERNAME); });
        findMemberByEmail(member.getEmail()).ifPresent(m -> { throw new UnauthorizedException(ExceptionType.DUPLICATED_EMAIL); });
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);
        Member savedMember = userAuthRepository.save(member);
        return savedMember;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteMember(Member member) {
        log.info("[DELETE MEMBER] userId: {} / socialType: {} / socialId: {}",
            member.getId(), member.getSocialType(), member.getSocialId());
        userAuthRepository.delete(member);
    }
}
