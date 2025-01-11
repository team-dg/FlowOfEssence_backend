package com.lolclone.chatinfra.service.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.domain.MemberDomainEvent;
import com.lolclone.chatdomain.domain.MemberStatus;
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

    public Member getOrThrow(final UUID id) {
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }

    public boolean existsById(final UUID id) {
        return memberRepository.existsById(id);
    }

    /**
     * 여러 ID에 해당하는 사용자들을 한 번에 조회
     * @param ids 조회할 사용자 ID 목록
     * @return 조회된 사용자 Map (ID를 key로 사용)
     */
    public Map<UUID, Member> getMembersByIds(Collection<UUID> ids) {
        List<Member> members = memberRepository.findAllById(ids);
        
        // 조회된 결과가 요청한 ID 개수와 다르면 예외 발생
        if (members.size() != ids.size())
            throw new NotFoundException(ExceptionType.USER_NOT_FOUND);
        
        return members.stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateMemberStatus(final UUID id, final MemberStatus status) {
        Member member = getOrThrow(id);
        member.updateStatus(status);
        // 상태 변경 -> 매칭 서버로 이벤트 발행
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateMemberLastLogin(final UUID id, final String lastLogin) {
        Member member = getOrThrow(id);
        member.updateLastLogin(lastLogin);
        // 마지막 로그인 시간 변경 -> 매칭 서버로 이벤트 발행
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Member findByIdAndCreateMember(final UUID id, final Member member) {
        return memberRepository.findById(id).orElseGet(() -> memberRepository.save(member));
    }

    /**
     * 닉네임으로 사용자 검색 (특정 사용자 제외)
     */
    public List<Member> searchByNickname(final String nickname, final UUID excludeUserId) {
        return memberRepository.findByNicknameContainingAndIdNot(nickname, excludeUserId);
    }

    @Transactional
    public void createMember(final UUID id, final String nickname) {
        Member member = Member.of(id, nickname);
        Member savedMember = findByIdAndCreateMember(id, member);
        List<MemberDomainEvent> events = savedMember.createMember();
        domainEventPublisher.publish(savedMember, events);
    }

    @Transactional
    public void undoCreateMember(final UUID id) {
        memberRepository.findById(id)
            .ifPresent(member -> {
                memberRepository.delete(member);
                List<MemberDomainEvent> events = member.undoCreateMember();
                domainEventPublisher.publish(member, events);
            });
    }
}
