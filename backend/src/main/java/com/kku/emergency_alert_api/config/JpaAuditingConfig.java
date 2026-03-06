package com.kku.emergency_alert_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// เปิดใช้ JPA Auditing เพื่อให้ @CreatedDate / @LastModifiedDate ทำงานอัตโนมัติ
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
