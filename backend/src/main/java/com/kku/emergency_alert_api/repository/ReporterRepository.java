package com.kku.emergency_alert_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kku.emergency_alert_api.entity.ReporterEntity;

@Repository
public interface ReporterRepository extends JpaRepository<ReporterEntity, Long> {

    // ค้นหาผู้แจ้งเหตุด้วย email
    Optional<ReporterEntity> findByEmail(String email);

    // เช็คว่า email นี้มีอยู่แล้วหรือไม่
    boolean existsByEmail(String email);
}
