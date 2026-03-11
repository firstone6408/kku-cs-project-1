package com.kku.emergency_alert_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kku.emergency_alert_api.annotation.RequireRole;
import com.kku.emergency_alert_api.constant.UserRoleEnum;
import com.kku.emergency_alert_api.dto.admin.AdminRequestDTO;
import com.kku.emergency_alert_api.dto.admin.AdminResponseDTO;
import com.kku.emergency_alert_api.dto.admin.AdminUpdateRequestDTO;
import com.kku.emergency_alert_api.service.admin.contract.AdminService;
import com.kku.emergency_alert_api.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // สร้าง admin ใหม่ — ไม่ต้อง lสร้าง api สำหรับ crud account admin (นอกจากการ
    // create ที่เหลือต้องเป็น admin เท่านั้นแก้)ogin (exclude จาก AuthInterceptor
    // ใน WebConfig)
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> create(@Valid @RequestBody AdminRequestDTO requestDTO) {
        return ApiResponse.success("Admin created", adminService.create(requestDTO));
    }

    // ดู admin ทั้งหมด — ต้องเป็น ADMIN เท่านั้น
    @GetMapping
    @RequireRole({ UserRoleEnum.ADMIN })
    public ResponseEntity<ApiResponse<List<AdminResponseDTO>>> getAll() {
        return ApiResponse.success(adminService.getAll());
    }

    // ดู admin ตาม id — ต้องเป็น ADMIN เท่านั้น
    @GetMapping("/{id}")
    @RequireRole({ UserRoleEnum.ADMIN })
    public ResponseEntity<ApiResponse<AdminResponseDTO>> getById(@PathVariable Long id) {
        return ApiResponse.success(adminService.getById(id));
    }

    // แก้ไข admin — ต้องเป็น ADMIN เท่านั้น
    @PutMapping("/{id}")
    @RequireRole({ UserRoleEnum.ADMIN })
    public ResponseEntity<ApiResponse<AdminResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateRequestDTO requestDTO) {
        return ApiResponse.success("Admin updated", adminService.update(id, requestDTO));
    }

    // ลบ admin — ต้องเป็น ADMIN เท่านั้น
    @DeleteMapping("/{id}")
    @RequireRole({ UserRoleEnum.ADMIN })
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        adminService.delete(id);
        return ApiResponse.success("Admin deleted");
    }
}
