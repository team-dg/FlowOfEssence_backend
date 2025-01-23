package com.lolclone.chatdomain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, UUID>{
    //List<Member> findByNicknameContainingAndIdNot(String nickname, UUID excludeUserId);
}
