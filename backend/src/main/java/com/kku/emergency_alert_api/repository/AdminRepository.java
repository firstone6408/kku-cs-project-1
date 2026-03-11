package com.kku.emergency_alert_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kku.emergency_alert_api.entity.AdminEntity;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    // ค้นหา admin ด้วย email
    Optional<AdminEntity> findByEmail(String email);

    // เช็คว่า email นี้มีอยู่แล้วหรือไม่
    boolean existsByEmail(String email);
}
