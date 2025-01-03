package com.lolclone.chatdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long>{
    
}
