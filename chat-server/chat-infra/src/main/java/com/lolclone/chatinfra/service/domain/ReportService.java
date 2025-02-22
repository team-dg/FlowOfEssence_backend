package com.lolclone.chatinfra.service.domain;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.Report;
import com.lolclone.chatdomain.domain.ReportReason;
import com.lolclone.chatdomain.domain.ReportStatus;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.repository.ReportRepository;
import com.lolclone.chatinfra.exception.commonexception.BadRequestException;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final ApplicationEventPublisher eventPublisher;

    // public Report getOrThrow(final Long id) {
    //     return reportRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.REPORT_NOT_FOUND));
    // }

    // /**
    //  * 신고 생성
    //  */
    // @Transactional(propagation = Propagation.MANDATORY)
    // public Report createReport(final Member reporter, final Member reportedUser, final ReportReason reason, final String detail) {
    //     validateNotSelfReport(reporter, reportedUser);
    //     Report report = Report.create(reporter, reportedUser, reason, detail);
    //     Report savedReport = reportRepository.save(report);
    //     // 신고 생성 이벤트 발행 (필요한 경우)
    //     return savedReport;
    // }

    // /**
    //  * 신고 처리 시작
    //  */
    // @Transactional(propagation = Propagation.MANDATORY)
    // public void processReport(final Long reportId) {
    //     Report report = getOrThrow(reportId);
    //     report.process();
    //     // 신고 처리 시작 이벤트 발행 (필요한 경우)
    // }

    // /**
    //  * 신고 처리 완료
    //  */
    // @Transactional(propagation = Propagation.MANDATORY)
    // public void completeReport(final Long reportId) {
    //     Report report = getOrThrow(reportId);
    //     report.complete();
    //     // 신고 처리 완료 이벤트 발행 (필요한 경우)
    // }

    // /**
    //  * 신고 거절
    //  */
    // @Transactional(propagation = Propagation.MANDATORY)
    // public void rejectReport(final Long reportId) {
    //     Report report = getOrThrow(reportId);
    //     report.reject();
    //     // 신고 거절 이벤트 발행 (필요한 경우)
    // }

    // /**
    //  * 사용자가 신고한 목록 조회
    //  */
    // public List<Report> getReportsByReporter(final Member reporter) {
    //     return reportRepository.findByReporter(reporter);
    // }

    // /**
    //  * 특정 사용자에 대한 신고 목록 조회
    //  */
    // public List<Report> getReportsAgainstUser(final Member reportedUser) {
    //     return reportRepository.findByReportedUser(reportedUser);
    // }

    // /**
    //  * 특정 상태의 신고 목록 조회
    //  */
    // public List<Report> getReportsByStatus(final ReportStatus status) {
    //     return reportRepository.findByStatus(status);
    // }

    // private void validateNotSelfReport(final Member reporter, final Member reportedUser) {
    //     if (reporter.equals(reportedUser)) {
    //         throw new BadRequestException(ExceptionType.SELF_REPORT_NOT_ALLOWED);
    //     }
    // }
}
