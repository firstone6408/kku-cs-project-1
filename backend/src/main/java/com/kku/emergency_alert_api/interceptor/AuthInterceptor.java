package com.kku.emergency_alert_api.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.kku.emergency_alert_api.constant.UserRoleEnum;
import com.kku.emergency_alert_api.context.user.impl.ThreadLocalUserContextProvider;
import com.kku.emergency_alert_api.exception.UnauthorizedException;
import com.kku.emergency_alert_api.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * AuthInterceptor — ตรวจสอบ JWT token ก่อนเข้า Controller
 *
 * JWT payload format: "<userId>:<role>"
 * เช่น "1:ADMIN", "5:REPORTER", "3:STAFF"
 *
 * ปรับปรุงจาก POS:
 * - ใช้ SLF4J Logger แทน System.out.println
 * - รองรับ 3 ประเภท user (เก็บ userId + role แทน entity ตรงๆ)
 * - format payload เป็น "id:role" เพื่อไม่ต้อง query DB ใน interceptor
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    private final JwtUtil jwtUtil;
    private final ThreadLocalUserContextProvider userContextProvider;

    public AuthInterceptor(JwtUtil jwtUtil, ThreadLocalUserContextProvider userContextProvider) {
        this.jwtUtil = jwtUtil;
        this.userContextProvider = userContextProvider;
    }

    /*
     * preHandle คือ ทำก่อนไปถึง Controller
     * 
     * หมายเหตุ การใส่ @NonNull คือเพื่อที่จะการันตีว่า มันจะไม่เป็น Null แน่นอน
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        try {
            logger.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());

            // แกะ Authorization header
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new UnauthorizedException("No Authorization header");
            }

            // แกะ token
            String token = authHeader.substring(7);

            // ตรวจสอบ token ถูกต้องหรือไม่
            if (!jwtUtil.isTokenValid(token)) {
                throw new UnauthorizedException("Invalid token");
            }

            // ตรวจสอบ token หมดอายุหรือยัง
            if (jwtUtil.isTokenExpired(token)) {
                throw new UnauthorizedException("Token expired");
            }

            // แกะ payload — format: "userId:role" (เช่น "1:ADMIN")
            String payload = jwtUtil.getDataFromToken(token);
            if (payload == null || !payload.contains(":")) {
                throw new UnauthorizedException("Invalid token payload");
            }

            String[] parts = payload.split(":");
            Long userId = Long.parseLong(parts[0]);
            UserRoleEnum role = UserRoleEnum.valueOf(parts[1]);

            // เก็บ user context สำหรับ request นี้
            userContextProvider.setCurrentUser(userId, role);

            return true;
        } catch (UnauthorizedException e) {
            userContextProvider.clear();
            throw e;
        } catch (Exception e) {
            userContextProvider.clear();
            throw new UnauthorizedException("Authentication failed");
        }
    }

    // ทำงาน หลัง Controller ทำงานเสร็จ แต่ก่อน Response ถูกส่งกลับ
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable ModelAndView modelAndView) {
        logger.debug("Request processed: {} {} status={}", request.getMethod(), request.getRequestURI(),
                response.getStatus());
    }

    // ทำงาน หลัง Controller ทำงานเสร็จ แต่ก่อน Response ถูกส่งกลับ
    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable Exception ex) {
        // Clear เสมอ ไม่ว่าจะสำเร็จหรือไม่ (ป้องกัน memory leak)
        userContextProvider.clear();
    }
}
