package com.lolclone.chatdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.domain.member.MemberId;

public interface MemberRepository extends JpaRepository<Member, MemberId>{
    //List<Member> findByNicknameContainingAndIdNot(String nickname, UUID excludeUserId);
}
