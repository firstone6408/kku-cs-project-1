package com.kku.emergency_alert_api.service.admin.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.kku.emergency_alert_api.dto.admin.AdminRequestDTO;
import com.kku.emergency_alert_api.dto.admin.AdminResponseDTO;
import com.kku.emergency_alert_api.dto.admin.AdminUpdateRequestDTO;
import com.kku.emergency_alert_api.entity.AdminEntity;
import com.kku.emergency_alert_api.exception.ResourceNotFoundException;
import com.kku.emergency_alert_api.repository.AdminRepository;
import com.kku.emergency_alert_api.service.admin.contract.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AdminResponseDTO create(AdminRequestDTO requestDTO) {
        // เช็คว่า email มีอยู่แล้วหรือไม่
        if (adminRepository.existsByEmail(requestDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // สร้าง entity
        AdminEntity adminToCreate = new AdminEntity();
        adminToCreate.setEmail(requestDTO.getEmail());
        adminToCreate.setFullName(requestDTO.getFullName());
        adminToCreate.setPasswordHash(passwordEncoder.encode(requestDTO.getPassword()));

        // บันทึก
        AdminEntity saved = adminRepository.save(adminToCreate);
        return AdminResponseDTO.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminResponseDTO> getAll() {
        return adminRepository.findAll()
                .stream()
                .map(AdminResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponseDTO getById(Long id) {
        AdminEntity admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
        return AdminResponseDTO.fromEntity(admin);
    }

    @Override
    @Transactional
    public AdminResponseDTO update(Long id, AdminUpdateRequestDTO requestDTO) {
        AdminEntity adminToUpdate = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        // อัปเดตเฉพาะ field ที่ส่งมา (partial update)
        if (requestDTO.getEmail() != null) {
            // เช็คว่า email ไม่ซ้ำกับคนอื่น
            if (!adminToUpdate.getEmail().equals(requestDTO.getEmail())
                    && adminRepository.existsByEmail(requestDTO.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }
            adminToUpdate.setEmail(requestDTO.getEmail());
        }

        if (requestDTO.getFullName() != null) {
            adminToUpdate.setFullName(requestDTO.getFullName());
        }

        if (requestDTO.getPassword() != null) {
            adminToUpdate.setPasswordHash(passwordEncoder.encode(requestDTO.getPassword()));
        }

        AdminEntity saved = adminRepository.save(adminToUpdate);
        return AdminResponseDTO.fromEntity(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }
}
