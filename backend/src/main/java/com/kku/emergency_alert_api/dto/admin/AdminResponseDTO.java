package com.kku.emergency_alert_api.dto.admin;

import java.time.LocalDateTime;

import com.kku.emergency_alert_api.entity.AdminEntity;

import lombok.Getter;
import lombok.Setter;

// Response DTO — ไม่เปิดเผย password_hash
@Getter
@Setter
public class AdminResponseDTO {

    private Long id;
    private String email;
    private String fullName;
    private LocalDateTime createdAt;

    // แปลง Entity → DTO (ไม่ส่ง password กลับไป client)
    public static AdminResponseDTO fromEntity(AdminEntity entity) {
        AdminResponseDTO dto = new AdminResponseDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFullName(entity.getFullName());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
