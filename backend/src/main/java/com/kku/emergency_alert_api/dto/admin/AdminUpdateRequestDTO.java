package com.kku.emergency_alert_api.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/*
 * DTO สำหรับ update admin — ทุก field เป็น optional
 * ส่งเฉพาะ field ที่ต้องการแก้ (field ที่ไม่ส่ง = ไม่แก้)
 */
@Getter
@Setter
public class AdminUpdateRequestDTO {

    @Email(message = "Invalid email format")
    private String email;

    private String fullName;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
