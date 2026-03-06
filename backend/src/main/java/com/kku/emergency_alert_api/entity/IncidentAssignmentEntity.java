package com.kku.emergency_alert_api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kku.emergency_alert_api.constant.AssignmentStatusEnum;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
 * มอบหมายทีม ↔ รายการแจ้งเหตุ (Many-to-Many)
 *
 * 1 รายการแจ้งเหตุ มีหลายทีมช่วยได้ (ตาม max_teams)
 * 1 ทีม รับหลายรายการได้ (ถ้าว่าง)
 *
 * สถานะ: ACCEPTED → COMPLETED / CANCELLED
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "incident_assignments")
public class IncidentAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // รายการแจ้งเหตุที่รับ
    @ManyToOne
    @JoinColumn(name = "incident_id", nullable = false)
    private IncidentEntity incident;

    // ทีมที่รับงาน
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    // สถานะการมอบหมาย
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssignmentStatusEnum status = AssignmentStatusEnum.ACCEPTED;

    @CreatedDate
    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;

    // เวลาที่ทำเสร็จ (NULL = ยังไม่เสร็จ)
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // ความสัมพันธ์: 1 assignment → หลาย snapshot สมาชิก
    @OneToMany(mappedBy = "assignment")
    private List<IncidentAssignmentMemberEntity> memberSnapshots = new ArrayList<>();
}
