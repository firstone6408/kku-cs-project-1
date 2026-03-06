package com.kku.emergency_alert_api.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kku.emergency_alert_api.constant.BlockActionEnum;
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
 * ประวัติการ block/unblock ผู้ใช้
 *
 * เก็บทุกครั้งที่ admin ทำการ block หรือ unblock
 * ใช้ target_type + target_id (polymorphic) เพราะ reporter กับ staff แยก table
 * reason บังคับกรอกเพื่อเก็บเหตุผลทุกครั้ง
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "block_history")
public class BlockHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ประเภทเป้าหมาย: REPORTER หรือ STAFF
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private UserRoleEnum targetType;

    // ID ของเป้าหมาย (FK ไป reporters หรือ staff)
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    // admin ที่ทำการ block/unblock
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private AdminEntity admin;

    // การกระทำ: BLOCK หรือ UNBLOCK
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private BlockActionEnum action;

    // เหตุผล (บังคับกรอก)
    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
