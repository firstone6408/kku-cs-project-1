package com.kku.emergency_alert_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Base exception สำหรับ business logic ทั้งหมด
// ให้ exception อื่นๆ extend จาก class นี้
public class BusinessException extends ResponseStatusException {

    public BusinessException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
