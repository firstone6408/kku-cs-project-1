package com.kku.emergency_alert_api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kku.emergency_alert_api.constant.TeamStatusEnum;

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

// ทีมพนักงาน — มีสถานะว่าง/ปฏิบัติงาน
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "teams")
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    // สถานะทีม: AVAILABLE (ว่าง) / ON_MISSION (กำลังปฏิบัติงาน)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TeamStatusEnum status = TeamStatusEnum.AVAILABLE;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ความสัมพันธ์: 1 ทีม → หลายสมาชิก (ผ่าน team_members)
    @OneToMany(mappedBy = "team")
    private List<TeamMemberEntity> members = new ArrayList<>();

    // ความสัมพันธ์: 1 ทีม → หลาย assignment
    @OneToMany(mappedBy = "team")
    private List<IncidentAssignmentEntity> assignments = new ArrayList<>();
}
