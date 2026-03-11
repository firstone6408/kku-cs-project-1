package com.kku.emergency_alert_api.exception;

import org.springframework.http.HttpStatus;

// 404 Not Found — หาข้อมูลไม่เจอ
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
