package com.kku.emergency_alert_api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
 * ประเภทเหตุฉุกเฉิน — จัดการโดย Admin
 * เช่น: อุบัติเหตุ, ไฟไหม้, เจ็บป่วยเฉียบพลัน
 *
 * priority_level กำหนดลำดับความสำคัญ (1 = สำคัญสุด)
 * ใช้เรียงลำดับรายการแจ้งเหตุที่พนักงานเห็น
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "incident_types")
public class IncidentTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    // ลำดับความสำคัญ (1 = สำคัญสุด)
    @Column(name = "priority_level", nullable = false)
    private Integer priorityLevel;

    // เปิด/ปิดการใช้งาน (admin สามารถซ่อนประเภทได้โดยไม่ต้องลบ)
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ความสัมพันธ์: 1 ประเภท → หลายรายการแจ้งเหตุ
    @OneToMany(mappedBy = "incidentType")
    private List<IncidentEntity> incidents = new ArrayList<>();
}
