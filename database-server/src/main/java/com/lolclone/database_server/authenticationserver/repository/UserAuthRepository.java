package com.lolclone.database_server.authenticationserver.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.lolclone.common_module.commonexception.NotFoundException;
import com.lolclone.common_module.exception.domain.ExceptionType;
import com.lolclone.database_server.authenticationserver.domain.SocialType;
import com.lolclone.database_server.authenticationserver.domain.Member;

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
