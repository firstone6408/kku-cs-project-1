package com.kku.emergency_alert_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // อนุญาตทุก endpoint
                .allowedOrigins("http://localhost:3000") // อนุญาตทุก origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // method ที่อนุญาต
                .allowedHeaders("Authorization", "Content-Type")// header ที่อนุญาต
                .allowCredentials(false); // ไม่รับ cookie / session
    }
}
