package com.kku.emergency_alert_api.context.user;

import com.kku.emergency_alert_api.constant.UserRoleEnum;

/**
 * สำหรับเข้าถึงข้อมูล user ปัจจุบันใน request scope
 * ใช้ใน Service layer เพื่อดึง current user โดยไม่ต้องรับ parameter จาก Controller
 *
 * เนื่องจากระบบมี 3 ประเภท user (Reporter, Staff, Admin) ที่แยก table กัน
 * จึงเก็บ id + role เพื่อให้ Service layer ไปดึง entity จาก repository ที่ถูกต้อง
 */
public interface UserContextProvider {

    /**
     * ดึง ID ของ user ที่ login อยู่ใน request ปัจจุบัน
     *
     * @return ID ของ user (ใช้ร่วมกับ role เพื่อ query จาก repository ที่ถูกต้อง)
     * @throws IllegalStateException ถ้ายังไม่ได้ login (ไม่มี context)
     */
    Long getCurrentUserId();

    /**
     * ดึง role ของ user ที่ login อยู่ใน request ปัจจุบัน
     *
     * @return role ของ user (REPORTER, STAFF, หรือ ADMIN)
     * @throws IllegalStateException ถ้ายังไม่ได้ login (ไม่มี context)
     */
    UserRoleEnum getCurrentUserRole();
}
