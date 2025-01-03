package com.lolclone.chatinfra.service.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.domain.MemberDomainEvent;
import com.lolclone.chatdomain.repository.MemberRepository;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;
import com.lolclone.chatinfra.saga.producer.ChatDomainEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final ChatDomainEventPublisher domainEventPublisher;

    public Member getOrThrow(UUID id) {
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Member findByIdAndCreateMember(UUID id, Member member) {
        return memberRepository.findById(id).orElseGet(() -> memberRepository.save(member));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void createMember(UUID id, String nickname) {
        Member member = Member.of(id, nickname);
        Member savedMember = findByIdAndCreateMember(id, member);
        List<MemberDomainEvent> events = savedMember.createMember();
        domainEventPublisher.publish(savedMember, events);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void undoCreateMember(UUID id) {
        memberRepository.findById(id)
            .ifPresent(member -> {
                memberRepository.delete(member);
                List<MemberDomainEvent> events = member.undoCreateMember();
                domainEventPublisher.publish(member, events);
            });
    }
}
