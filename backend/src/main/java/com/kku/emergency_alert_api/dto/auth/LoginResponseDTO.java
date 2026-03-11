package com.kku.emergency_alert_api.dto.auth;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

// DTO สำหรับ response หลัง login/register สำเร็จ (auto-login)
@Getter
@Setter
public class LoginResponseDTO {

    private String token;

    // ข้อมูล user ที่จะส่งกลับ
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String role;         // "REPORTER" | "STAFF"
    private Boolean isBlocked;
    private LocalDateTime createdAt;
}
