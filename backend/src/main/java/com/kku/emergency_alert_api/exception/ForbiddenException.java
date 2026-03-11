package com.kku.emergency_alert_api.exception;

import org.springframework.http.HttpStatus;

// 403 Forbidden — มี token แต่ไม่มีสิทธิ์เข้าถึง
public class ForbiddenException extends BusinessException {

    public ForbiddenException(String reason) {
        super(HttpStatus.FORBIDDEN, reason);
    }
}
