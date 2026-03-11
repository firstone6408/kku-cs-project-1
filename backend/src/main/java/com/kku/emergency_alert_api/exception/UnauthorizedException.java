package com.kku.emergency_alert_api.exception;

import org.springframework.http.HttpStatus;

// 401 Unauthorized — ไม่มี token หรือ token ไม่ถูกต้อง
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String reason) {
        super(HttpStatus.UNAUTHORIZED, reason);
    }
}
