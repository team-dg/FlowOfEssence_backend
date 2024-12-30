package com.lolclone.authenticationmanagementdomain.repository;

import java.util.Optional;
import java.util.UUID;

import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUsername(String username);
    
    Optional<Member> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    Optional<Member> findById(UUID id);

    Member save(Member member);

    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    void delete(Member member);
}
