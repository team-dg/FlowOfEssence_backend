package com.lolclone.chatinfra.service.domain;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 여러 ID에 해당하는 사용자들을 한 번에 조회
     * @param ids 조회할 사용자 ID 목록
     * @return 조회된 사용자 Map (ID를 key로 사용)
     */
    // public Map<UUID, Member> getMembersByIds(Collection<UUID> ids) {
    //     List<Member> members = memberRepository.findAllById(ids);
        
    //     // 조회된 결과가 요청한 ID 개수와 다르면 예외 발생
    //     if (members.size() != ids.size())
    //         throw new NotFoundException(ExceptionType.USER_NOT_FOUND);
        
    //     return members.stream()
    //             .collect(Collectors.toMap(Member::getId, Function.identity()));
    // }

    /**
     * 멤버의 온라인 상태를 업데이트합니다.
     */
    // public void updateMemberOnlineStatus(UUID userId, boolean online) {
    //     Member member = findMemberById(userId);
    //     member.updateLoginStatus(online);
        
    //     if (online) {
    //         member.updateLastLogin(LocalDateTime.now().toString());
    //     }
        
    //     memberRepository.save(member);
    //     // eventPublisher.publish(new MemberStatusChangedEvent(userId, online));
    // }

    // /**
    //  * 멤버의 태그를 관리합니다.
    //  */
    // public void manageMemberTags(UUID userId, String tag, boolean add) {
    //     Member member = findMemberById(userId);
        
    //     if (add) {
    //         member.addTag(tag);
    //     } else {
    //         member.removeTag(tag);
    //     }
        
    //     memberRepository.save(member);
    // }

    // /**
    //  * 닉네임으로 사용자 검색 (특정 사용자 제외)
    //  */
    // // public List<Member> searchByNickname(final String nickname, final UUID excludeUserId) {
    // //     return memberRepository.findByNicknameContainingAndIdNot(nickname, excludeUserId);
    // // }

    @Transactional(propagation = Propagation.MANDATORY)
    public Member findByIdAndCreateMember(final UUID id, final Member member) {
        return memberRepository.findById(id).orElseGet(() -> memberRepository.save(member));
    }

    @Transactional
    public Member createMember(UUID userId, String nickname) {
        Member member = Member.builder()
                .id(userId)
                .nickname(nickname)
                .build();

        Member savedMember = findByIdAndCreateMember(userId, member);

        return savedMember;
    }

    @Transactional
    public void undoCreateMember(UUID userId) {
        memberRepository.findById(userId)
            .ifPresent(member -> {
                memberRepository.delete(member);
            });
    }

    // // 내부 헬퍼 메서드
    // private Member findMemberById(UUID userId) {
    //     return memberRepository.findById(MemberId.of(userId))
    //         .orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    // }
}
