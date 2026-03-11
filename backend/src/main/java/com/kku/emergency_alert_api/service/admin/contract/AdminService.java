package com.kku.emergency_alert_api.service.admin.contract;

import java.util.List;

import com.kku.emergency_alert_api.dto.admin.AdminRequestDTO;
import com.kku.emergency_alert_api.dto.admin.AdminResponseDTO;
import com.kku.emergency_alert_api.dto.admin.AdminUpdateRequestDTO;

/**
 * จัดการข้อมูลผู้ดูแลระบบ (Admin) — CRUD operations
 */
public interface AdminService {

    /**
     * สร้าง admin ใหม่ — เช็ค email ซ้ำ และ hash password ก่อนบันทึก
     *
     * @param requestDTO ข้อมูล admin ที่ต้องการสร้าง (email, fullName, password)
     * @return ข้อมูล admin ที่สร้างเสร็จแล้ว (ไม่รวม password)
     * @throws ResponseStatusException 409 ถ้า email ซ้ำ
     */
    AdminResponseDTO create(AdminRequestDTO requestDTO);

    /**
     * ดึงรายชื่อ admin ทั้งหมดในระบบ
     *
     * @return รายการ admin ทั้งหมด
     */
    List<AdminResponseDTO> getAll();

    /**
     * ดึงข้อมูล admin ตาม ID
     *
     * @param id ID ของ admin ที่ต้องการ
     * @return ข้อมูล admin
     * @throws ResourceNotFoundException ถ้าไม่พบ admin
     */
    AdminResponseDTO getById(Long id);

    /**
     * แก้ไขข้อมูล admin — รองรับ partial update (ส่งเฉพาะ field ที่ต้องการแก้)
     *
     * @param id         ID ของ admin ที่ต้องการแก้ไข
     * @param requestDTO ข้อมูลที่ต้องการแก้ (email, fullName, password — ส่งเฉพาะที่ต้องการ)
     * @return ข้อมูล admin หลังแก้ไข
     * @throws ResourceNotFoundException ถ้าไม่พบ admin
     * @throws ResponseStatusException   409 ถ้า email ซ้ำกับคนอื่น
     */
    AdminResponseDTO update(Long id, AdminUpdateRequestDTO requestDTO);

    /**
     * ลบ admin ตาม ID
     *
     * @param id ID ของ admin ที่ต้องการลบ
     * @throws ResourceNotFoundException ถ้าไม่พบ admin
     */
    void delete(Long id);
}
