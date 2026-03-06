package com.kku.emergency_alert_api.constant;

// บทบาทผู้ใช้ทั้ง 3 ประเภท (ใช้ใน JWT payload + sender_type + target_type)
public enum UserRoleEnum {
    REPORTER, // ผู้แจ้งเหตุ (นิสิต / บุคลากร)
    STAFF, // พนักงาน (อาสา / เจ้าหน้าที่)
    ADMIN // ผู้ดูแลระบบ
}
