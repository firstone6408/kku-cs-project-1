package com.kku.emergency_alert_api.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kku.emergency_alert_api.constant.ReportStatusEnum;
import com.kku.emergency_alert_api.constant.UserRoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
 * ระบบ report ผู้ใช้
 *
 * Staff report Reporter (เช่น แจ้งเหตุมัว)
 * Reporter report Staff (เช่น พฤติกรรมไม่เหมาะสม)
 * Staff report Staff ก็ได้
 *
 * เชื่อมกับ incident_id (optional) เพื่อระบุเหตุที่เกี่ยวข้อง
 * admin review → เปลี่ยนสถานะเป็น REVIEWED / DISMISSED
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_reports")
public class UserReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ประเภทคนที่กด report: REPORTER หรือ STAFF
    @Enumerated(EnumType.STRING)
    @Column(name = "reporter_type", nullable = false)
    private UserRoleEnum reporterType;

    // ID ของคนที่กด report
    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    // ประเภทคนที่ถูก report: REPORTER หรือ STAFF
    @Enumerated(EnumType.STRING)
    @Column(name = "reported_type", nullable = false)
    private UserRoleEnum reportedType;

    // ID ของคนที่ถูก report
    @Column(name = "reported_id", nullable = false)
    private Long reportedId;

    // เหตุที่เกี่ยวข้อง (optional)
    @ManyToOne
    @JoinColumn(name = "incident_id")
    private IncidentEntity incident;

    // เหตุผลที่ report (บังคับกรอก)
    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    // สถานะ: PENDING → REVIEWED / DISMISSED
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatusEnum status = ReportStatusEnum.PENDING;

    // admin เขียนสรุปผลการ review (optional)
    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;

    // admin ที่ review (optional, มีค่าเมื่อ status != PENDING)
    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private AdminEntity reviewedBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // เวลาที่ review (NULL = ยัง review)
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
}
