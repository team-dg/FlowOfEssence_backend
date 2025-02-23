package com.lolclone.authenticationmanagementdomain.repository;

import java.util.Optional;
import java.util.UUID;

import com.lolclone.authenticationmanagementdomain.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUsername(String username);
    
    Optional<Member> findByEmail(String email);

    Optional<Member> findBySocialName(String socialName);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    Optional<Member> findById(UUID id);

    void delete(Member member);
}
