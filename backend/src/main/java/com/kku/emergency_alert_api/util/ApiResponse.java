package com.kku.emergency_alert_api.util;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse<T> {

    private boolean ok;

    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    private ApiResponse(boolean ok, int status, String message, T data) {
        this.ok = ok;
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // --- private helper ---
    private static <T> ResponseEntity<ApiResponse<T>> build(boolean ok, HttpStatus httpStatus, String message, T data) {
        return ResponseEntity.status(httpStatus)
                .body(new ApiResponse<>(ok, httpStatus.value(), message, data));
    }

    // --- success ---
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return build(true, HttpStatus.OK, "ok", data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
        return build(true, HttpStatus.OK, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus httpStatus, T data) {
        return build(true, HttpStatus.OK, httpStatus.name(), data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus httpStatus, String message) {
        return build(true, HttpStatus.OK, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return build(true, HttpStatus.OK, message, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus httpStatus, String message, T data) {
        return build(true, HttpStatus.OK, message, data);
    }

    // --- error ---
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus httpStatus, String message) {
        return build(false, httpStatus, message, null);
    }

    // --- generic builder ---
    public static <T> ResponseEntity<ApiResponse<T>> of(boolean ok, HttpStatus status, String message, T data) {
        return build(ok, status, message, data);
    }
}
