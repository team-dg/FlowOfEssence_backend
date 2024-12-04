package com.lolclone.authentication_management_server.domain.repository;

import java.util.Optional;

import com.lolclone.authentication_management_server.common.commonexception.NotFoundException;
import com.lolclone.authentication_management_server.common.exception.domain.ExceptionType;
import com.lolclone.authentication_management_server.domain.entity.SocialType;
import org.springframework.data.repository.Repository;

import com.lolclone.authentication_management_server.domain.entity.Member;

public interface UserAuthRepository extends Repository<Member, Long> {
    default Member getOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    Optional<Member> findById(Long id);

    Member save(Member member);

    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    void delete(Member member);
}
