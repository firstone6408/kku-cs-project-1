package com.kku.emergency_alert_api.dto.auth;

import com.kku.emergency_alert_api.constant.StaffRoleEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// DTO สำหรับสมัคร Staff (พนักงาน/อาสา)
@Getter
@Setter
public class RegisterStaffRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotNull(message = "Role is required (VOLUNTEER or OFFICER)")
    private StaffRoleEnum role;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
