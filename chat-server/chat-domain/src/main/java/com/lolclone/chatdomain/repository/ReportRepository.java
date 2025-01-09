package com.lolclone.chatdomain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.domain.Report;
import com.lolclone.chatdomain.domain.ReportStatus;

public interface ReportRepository extends JpaRepository<Report, Long>{
    List<Report> findByReporter(Member reporter);
    List<Report> findByReportedUser(Member reportedUser);
    List<Report> findByStatus(ReportStatus status);
}
