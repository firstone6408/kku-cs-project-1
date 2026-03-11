package com.kku.emergency_alert_api.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kku.emergency_alert_api.annotation.RequireRole;
import com.kku.emergency_alert_api.constant.UserRoleEnum;
import com.kku.emergency_alert_api.context.user.UserContextProvider;
import com.kku.emergency_alert_api.exception.ForbiddenException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * RoleInterceptor — ตรวจสอบ role หลังจาก AuthInterceptor set context แล้ว
 *
 * ทำงานร่วมกับ @RequireRole annotation:
 * - ถ้า method มี @RequireRole → ตรวจว่า user มี role ตามที่กำหนดหรือไม่
 * - ถ้าไม่มี @RequireRole → ข้ามไป (ไม่ต้องเช็ค role)
 */
@Component
public class RoleInterceptor implements HandlerInterceptor {

    private final UserContextProvider userContextProvider;

    public RoleInterceptor(UserContextProvider userContextProvider) {
        this.userContextProvider = userContextProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);

        // ถ้าไม่มี @RequireRole → ข้ามไป
        if (requireRole == null) {
            return true;
        }

        UserRoleEnum currentRole = userContextProvider.getCurrentUserRole();

        // เช็คว่า user มี role ตามที่ annotation กำหนดหรือไม่
        for (UserRoleEnum allowedRole : requireRole.value()) {
            if (allowedRole.equals(currentRole)) {
                return true;
            }
        }

        throw new ForbiddenException("Access denied: insufficient role");
    }
}
