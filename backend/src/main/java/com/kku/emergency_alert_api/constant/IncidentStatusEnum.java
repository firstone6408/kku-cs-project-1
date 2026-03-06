package com.kku.emergency_alert_api.constant;

// สถานะรายการแจ้งเหตุ
public enum IncidentStatusEnum {
    REPORTED, // พึ่งแจ้ง, รอทีมรับ
    IN_PROGRESS, // มีทีมรับงานแล้ว
    NEED_MORE_TEAMS, // ต้องการทีมเพิ่ม
    COMPLETED, // ช่วยเหลือสำเร็จ
    CANCELLED // ยกเลิก
}
