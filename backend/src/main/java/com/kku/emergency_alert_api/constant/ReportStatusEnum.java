package com.kku.emergency_alert_api.constant;

// สถานะ report ผู้ใช้
public enum ReportStatusEnum {
    PENDING, // รอ admin review
    REVIEWED, // admin ดูแล้ว (อาจนำไปสู่การ block)
    DISMISSED // admin ปัดตก
}
