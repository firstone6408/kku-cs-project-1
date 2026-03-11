package com.kku.emergency_alert_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kku.emergency_alert_api.entity.StaffEntity;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Long> {

    // ค้นหาพนักงานด้วย email
    Optional<StaffEntity> findByEmail(String email);

    // เช็คว่า email นี้มีอยู่แล้วหรือไม่
    boolean existsByEmail(String email);
}
