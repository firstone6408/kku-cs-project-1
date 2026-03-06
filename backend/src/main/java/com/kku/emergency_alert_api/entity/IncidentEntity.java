package com.kku.emergency_alert_api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kku.emergency_alert_api.constant.IncidentStatusEnum;

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
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
 * รายการแจ้งเหตุ — หัวใจหลักของระบบ
 *
 * เก็บข้อมูล: ใครแจ้ง, ประเภทเหตุ, รายละเอียด, ตำแหน่ง GPS, สถานะ
 * max_teams: จำนวนทีมที่รับได้ (default 1, เพิ่มได้เมื่อกดขอทีมเพิ่ม)
 *
 * ใช้ BigDecimal สำหรับ lat/lng เพื่อความแม่นยำของพิกัด
 * (double อาจสูญเสียค่าทศนิยม ไม่เหมาะกับข้อมูลภูมิศาสตร์)
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "incidents")
public class IncidentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ผู้แจ้งเหตุ
    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private ReporterEntity reporter;

    // ประเภทเหตุฉุกเฉิน
    @ManyToOne
    @JoinColumn(name = "incident_type_id", nullable = false)
    private IncidentTypeEntity incidentType;

    // รายละเอียดเพิ่มเติม (optional)
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // เบอร์ติดต่อ (กรอกเอง หรือดึงจากบัญชี)
    @Column(name = "contact_phone", nullable = false)
    private String contactPhone;

    // พิกัด GPS สำหรับปักหมุดบนแผนที่
    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    // สถานะรายการแจ้งเหตุ
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private IncidentStatusEnum status = IncidentStatusEnum.REPORTED;

    // จำนวนทีมที่รับได้ (default 1, เพิ่มได้เมื่อกดขอทีมเพิ่ม)
    @Column(name = "max_teams", nullable = false)
    private Integer maxTeams = 1;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ความสัมพันธ์: 1 รายการ → หลายทีมที่มอบหมาย
    @OneToMany(mappedBy = "incident")
    private List<IncidentAssignmentEntity> assignments = new ArrayList<>();

    // ความสัมพันธ์: 1 รายการ → หลายหลักฐาน (รูป/วิดีโอ/เสียง)
    @OneToMany(mappedBy = "incident")
    private List<IncidentEvidenceEntity> evidence = new ArrayList<>();

    // ความสัมพันธ์: 1 รายการ → หลายข้อความแชท
    @OneToMany(mappedBy = "incident")
    @OrderBy("sentAt ASC")
    private List<ChatMessageEntity> chatMessages = new ArrayList<>();

    // ความสัมพันธ์: 1 รายการ → หลาย report
    @OneToMany(mappedBy = "incident")
    private List<UserReportEntity> reports = new ArrayList<>();
}
