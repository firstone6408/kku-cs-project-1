package com.kku.emergency_alert_api.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kku.emergency_alert_api.util.ApiResponse;

// จัดการ exception แบบ centralized — ทุก error จะถูกจับที่นี่แล้วส่ง ApiResponse กลับ
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // จับ validation errors (เช่น @NotBlank, @Email)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ApiResponse.error(HttpStatus.BAD_REQUEST, errors.toString());
    }

    // จับ ResponseStatusException (รวมถึง BusinessException ทั้งหมด)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatusCode statusCode = ex.getStatusCode();
        HttpStatus status = HttpStatus.resolve(statusCode.value());
        return ApiResponse.error(status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR, ex.getReason());
    }

    // จับ RuntimeException ทั่วไป
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        logger.error("Runtime exception: ", ex);
        return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // จับ Exception ทั่วไป (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        logger.error("Unexpected error: ", ex);
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }
}
