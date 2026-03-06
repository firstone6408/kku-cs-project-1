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
 * Snapshot สมาชิกที่ช่วยเหลือจริง
 *
 * เมื่อทีมกดรับงาน → ระบบ snapshot สมาชิกทุกคนในทีมตอนนั้นลงตารางนี้
 * แก้ปัญหา: ถ้า staff ย้ายทีมทีหลัง ก็ยังดูย้อนหลังได้ว่าตอนนั้นใครช่วยบ้าง
 * เก็บ role_at_time เพราะ staff อาจเปลี่ยน role ในอนาคต
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "incident_assignment_members")
public class IncidentAssignmentMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // assignment ที่สังกัด
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private IncidentAssignmentEntity assignment;

    // สมาชิกที่ช่วย
    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    // บทบาทของ staff ณ ตอนที่รับงาน (snapshot เพราะอาจเปลี่ยนได้)
    @Column(name = "role_at_time", nullable = false)
    private String roleAtTime;

    @CreatedDate
    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;
}
