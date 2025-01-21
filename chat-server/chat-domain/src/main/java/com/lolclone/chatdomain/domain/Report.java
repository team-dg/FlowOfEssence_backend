package com.lolclone.chatdomain.domain;

import java.time.LocalDateTime;

import com.lolclone.chatdomain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.member.Member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 신고 정보 관리 (생성, 조회)
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reports")
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private Member reportedUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportReason reason;

    @Column(length = 500)
    private String detail;  // 상세 신고 사유

    @Column(nullable = false)
    private LocalDateTime reportedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    private Report(Member reporter, Member reportedUser, ReportReason reason, String detail) {
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.reason = reason;
        this.detail = detail;
        this.reportedAt = LocalDateTime.now();
    }

    // 정적 팩토리 메서드
    public static Report create(Member reporter, Member reportedUser,
            ReportReason reason, String detail) {
        return new Report(reporter, reportedUser, reason, detail);
    }

    // 비즈니스 메서드
    public void process() {
        this.status = ReportStatus.PROCESSING;
    }

    public void complete() {
        this.status = ReportStatus.COMPLETED;
    }

    public void reject() {
        this.status = ReportStatus.REJECTED;
    }
}
