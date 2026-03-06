package com.kku.emergency_alert_api.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
 * ตารางเชื่อม Staff ↔ Team (Many-to-Many) + เก็บประวัติการย้ายทีม
 *
 * left_at = NULL → ยังอยู่ในทีมนี้
 * left_at != NULL → ย้ายออกแล้ว (เก็บเป็นประวัติ)
 *
 * เมื่อ staff ย้ายทีม:
 * 1. set left_at ของแถวเก่า = เวลาปัจจุบัน
 * 2. สร้างแถวใหม่ในทีมใหม่ (left_at = NULL)
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "team_members")
public class TeamMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ทีมที่สังกัด
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    // สมาชิก
    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    @CreatedDate
    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt;

    // NULL = ยังอยู่ในทีม, มีค่า = ย้ายออกแล้ว
    @Column(name = "left_at")
    private LocalDateTime leftAt;
}
