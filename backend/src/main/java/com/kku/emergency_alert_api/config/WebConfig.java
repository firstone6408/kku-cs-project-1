package com.kku.emergency_alert_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kku.emergency_alert_api.interceptor.AuthInterceptor;
import com.kku.emergency_alert_api.interceptor.RoleInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final RoleInterceptor roleInterceptor;

    public WebConfig(AuthInterceptor authInterceptor, RoleInterceptor roleInterceptor) {
        this.authInterceptor = authInterceptor;
        this.roleInterceptor = roleInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // อนุญาตทุก endpoint
                .allowedOrigins("http://localhost:3000") // อนุญาตทุก origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // method ที่อนุญาต
                .allowedHeaders("Authorization", "Content-Type")// header ที่อนุญาต
                .allowCredentials(false); // ไม่รับ cookie / session
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // AuthInterceptor — ตรวจ JWT token ทุก request ใน /api/**
        // ยกเว้น: Swagger, auth endpoints
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**") // apply ทุก endpoint
                .excludePathPatterns( // ยกเว้น path ที่ไม่อยากดัก
                        "/api/auth/**",
                        "/api/admin/create",
                        "/swagger-ui/**",
                        "/v3/api-docs/**");

        // RoleInterceptor — ตรวจ @RequireRole หลังจาก auth ผ่านแล้ว
        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/api/**");
    }
}
