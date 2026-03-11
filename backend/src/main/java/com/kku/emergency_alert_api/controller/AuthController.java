package com.kku.emergency_alert_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kku.emergency_alert_api.dto.auth.LoginRequestDTO;
import com.kku.emergency_alert_api.dto.auth.LoginResponseDTO;
import com.kku.emergency_alert_api.dto.auth.RegisterReporterRequestDTO;
import com.kku.emergency_alert_api.dto.auth.RegisterStaffRequestDTO;
import com.kku.emergency_alert_api.service.auth.contract.AuthService;
import com.kku.emergency_alert_api.util.ApiResponse;

import jakarta.validation.Valid;

// ทุก endpoint อยู่ใน /api/auth/** ที่ exclude จาก AuthInterceptor อยู่แล้ว
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // สมัครผู้แจ้งเหตุ
    @PostMapping("/reporter/register")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> registerReporter(
            @Valid @RequestBody RegisterReporterRequestDTO requestDTO) {
        return ApiResponse.success("Register success", authService.registerReporter(requestDTO));
    }

    // เข้าสู่ระบบผู้แจ้งเหตุ
    @PostMapping("/reporter/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> loginReporter(
            @Valid @RequestBody LoginRequestDTO requestDTO) {
        return ApiResponse.success("Login success", authService.loginReporter(requestDTO));
    }

    // สมัครพนักงาน/อาสา
    @PostMapping("/staff/register")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> registerStaff(
            @Valid @RequestBody RegisterStaffRequestDTO requestDTO) {
        return ApiResponse.success("Register success", authService.registerStaff(requestDTO));
    }

    // เข้าสู่ระบบพนักงาน/อาสา
    @PostMapping("/staff/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> loginStaff(
            @Valid @RequestBody LoginRequestDTO requestDTO) {
        return ApiResponse.success("Login success", authService.loginStaff(requestDTO));
    }
}
