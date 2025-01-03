package com.lolclone.userdomain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.userdomain.entity.Member;

public interface UserRepository extends JpaRepository<Member, UUID> {
    
}
