package com.lolclone.chatdomain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.Member;

public interface MemberRepository extends JpaRepository<Member, UUID>{
    List<Member> findByNicknameContainingAndIdNot(String nickname, UUID excludeUserId);
}
