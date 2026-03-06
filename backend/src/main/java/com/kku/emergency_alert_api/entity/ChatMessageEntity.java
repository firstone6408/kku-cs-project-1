package com.kku.emergency_alert_api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
 * ข้อความแชทในรายการแจ้งเหตุ
 *
 * 1 รายการแจ้งเหตุ = 1 ห้องแชท
 * ผู้ส่งเป็นได้ทั้ง REPORTER หรือ STAFF
 *
 * ใช้ sender_type + sender_id แทน FK ตรงๆ
 * เพราะ reporter กับ staff แยก table กัน (polymorphic relationship)
 *
 * message เป็น optional — กรณีส่งแค่ไฟล์แนบ
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "chat_messages")
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // รายการแจ้งเหตุที่แชทอยู่
    @ManyToOne
    @JoinColumn(name = "incident_id", nullable = false)
    private IncidentEntity incident;

    // ประเภทผู้ส่ง: REPORTER หรือ STAFF
    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private UserRoleEnum senderType;

    // ID ของผู้ส่ง (FK ไป reporters หรือ staff ขึ้นอยู่กับ sender_type)
    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    // ข้อความ (optional ถ้าส่งแค่ไฟล์แนบ)
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @CreatedDate
    @Column(name = "sent_at", updatable = false)
    private LocalDateTime sentAt;

    // ความสัมพันธ์: 1 ข้อความ → หลายไฟล์แนบ
    @OneToMany(mappedBy = "chatMessage")
    private List<ChatAttachmentEntity> attachments = new ArrayList<>();
}
