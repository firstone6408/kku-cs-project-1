package com.kku.emergency_alert_api.context.user.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kku.emergency_alert_api.constant.UserRoleEnum;
import com.kku.emergency_alert_api.context.user.UserContextProvider;

/*
 * เก็บข้อมูล user ใน ThreadLocal (request scope)
 *
 * ทำงานร่วมกับ AuthInterceptor:
 * 1. preHandle → set id + role จาก JWT
 * 2. Service layer → get id + role
 * 3. afterCompletion → clear (ป้องกัน memory leak)
 */
@Component
public class ThreadLocalUserContextProvider implements UserContextProvider {

    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<UserRoleEnum> userRoleHolder = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalUserContextProvider.class);

    // ใช้โดย AuthInterceptor เท่านั้น
    public void setCurrentUser(Long userId, UserRoleEnum role) {
        if (userId == null || role == null) {
            logger.warn("Attempting to set null userId or role");
            return;
        }
        userIdHolder.set(userId);
        userRoleHolder.set(role);
        logger.debug("Set user: id={}, role={} for thread: {}", userId, role, Thread.currentThread().getName());
    }

    @Override
    public Long getCurrentUserId() {
        Long userId = userIdHolder.get();
        if (userId == null) {
            logger.error("No user found in context for thread: {}", Thread.currentThread().getName());
            throw new IllegalStateException("No user context available. User may not be authenticated.");
        }
        return userId;
    }

    @Override
    public UserRoleEnum getCurrentUserRole() {
        UserRoleEnum role = userRoleHolder.get();
        if (role == null) {
            throw new IllegalStateException("No user role in context.");
        }
        return role;
    }

    // ใช้โดย AuthInterceptor เท่านั้น (ใน afterCompletion)
    public void clear() {
        Long userId = userIdHolder.get();
        userIdHolder.remove();
        userRoleHolder.remove();
        if (userId != null) {
            logger.debug("Cleared user: id={} from thread: {}", userId, Thread.currentThread().getName());
        }
    }

    public boolean hasUser() {
        return userIdHolder.get() != null;
    }
}
