package com.kku.emergency_alert_api.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kku.emergency_alert_api.constant.FileTypeEnum;

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
 * ไฟล์แนบในแชท (รูปภาพ / วิดีโอ / เสียง)
 *
 * 1 ข้อความแนบไฟล์ได้หลายไฟล์
 * ไฟล์จริงเก็บบน ImageKit CDN
 * โครงสร้างเหมือน IncidentEvidenceEntity
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "chat_attachments")
public class ChatAttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ข้อความที่แนบไฟล์
    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private ChatMessageEntity chatMessage;

    // ประเภทไฟล์: IMAGE, VIDEO, AUDIO
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileTypeEnum fileType;

    // ImageKit file ID — ใช้สำหรับจัดการไฟล์ (ลบ/แก้ไข) บน ImageKit
    @Column(name = "imagekit_file_id", nullable = false)
    private String imagekitFileId;

    // ImageKit CDN URL — ใช้แสดงผลฝั่ง client
    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    // ชื่อไฟล์ต้นฉบับ
    @Column(name = "file_name")
    private String fileName;

    // ขนาดไฟล์ (bytes)
    @Column(name = "file_size")
    private Long fileSize;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
