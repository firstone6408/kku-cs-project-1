package com.kku.emergency_alert_api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kku.emergency_alert_api.constant.StaffRoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// พนักงาน (อาสา / เจ้าหน้าที่) — ทำงานเป็นทีม
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "staff")
public class StaffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone", nullable = false)
    private String phone;

    // บทบาท: อาสา (VOLUNTEER) / เจ้าหน้าที่ (OFFICER)
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private StaffRoleEnum role;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // สถานะถูก block โดย admin (default = false)
    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ความสัมพันธ์: 1 staff → หลาย team (ผ่าน team_members, มีประวัติย้าย)
    @OneToMany(mappedBy = "staff")
    private List<TeamMemberEntity> teamMemberships = new ArrayList<>();

    // ความสัมพันธ์: 1 staff → หลาย snapshot ที่เคยช่วย
    @OneToMany(mappedBy = "staff")
    private List<IncidentAssignmentMemberEntity> assignmentHistory = new ArrayList<>();
}
