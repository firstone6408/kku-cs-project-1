package com.kku.emergency_alert_api.service.auth.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.kku.emergency_alert_api.constant.UserRoleEnum;
import com.kku.emergency_alert_api.dto.auth.LoginRequestDTO;
import com.kku.emergency_alert_api.dto.auth.LoginResponseDTO;
import com.kku.emergency_alert_api.dto.auth.RegisterReporterRequestDTO;
import com.kku.emergency_alert_api.dto.auth.RegisterStaffRequestDTO;
import com.kku.emergency_alert_api.entity.ReporterEntity;
import com.kku.emergency_alert_api.entity.StaffEntity;
import com.kku.emergency_alert_api.exception.UnauthorizedException;
import com.kku.emergency_alert_api.repository.ReporterRepository;
import com.kku.emergency_alert_api.repository.StaffRepository;
import com.kku.emergency_alert_api.service.auth.contract.AuthService;
import com.kku.emergency_alert_api.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final ReporterRepository reporterRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            ReporterRepository reporterRepository,
            StaffRepository staffRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.reporterRepository = reporterRepository;
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public LoginResponseDTO registerReporter(RegisterReporterRequestDTO dto) {
        // เช็ค email ซ้ำ
        if (reporterRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // สร้าง entity
        ReporterEntity reporterToCreate = new ReporterEntity();
        reporterToCreate.setEmail(dto.getEmail());
        reporterToCreate.setFullName(dto.getFullName());
        reporterToCreate.setPhone(dto.getPhone());
        reporterToCreate.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // บันทึก
        ReporterEntity saved = reporterRepository.save(reporterToCreate);

        // สร้าง token (format: "id:REPORTER")
        String token = jwtUtil.generateToken(saved.getId() + ":" + UserRoleEnum.REPORTER.name());

        return buildReporterResponse(saved, token);
    }

    @Override
    @Transactional
    public LoginResponseDTO registerStaff(RegisterStaffRequestDTO dto) {
        // เช็ค email ซ้ำ
        if (staffRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // สร้าง entity
        StaffEntity staffToCreate = new StaffEntity();
        staffToCreate.setEmail(dto.getEmail());
        staffToCreate.setFullName(dto.getFullName());
        staffToCreate.setPhone(dto.getPhone());
        staffToCreate.setRole(dto.getRole());
        staffToCreate.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // บันทึก
        StaffEntity saved = staffRepository.save(staffToCreate);

        // สร้าง token (format: "id:STAFF")
        String token = jwtUtil.generateToken(saved.getId() + ":" + UserRoleEnum.STAFF.name());

        return buildStaffResponse(saved, token);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDTO loginReporter(LoginRequestDTO dto) {
        // หา reporter ด้วย email
        ReporterEntity reporter = reporterRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        // เช็ค password
        if (!passwordEncoder.matches(dto.getPassword(), reporter.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        // เช็คว่าถูก block หรือไม่
        if (Boolean.TRUE.equals(reporter.getIsBlocked())) {
            throw new UnauthorizedException("Account is blocked");
        }

        // สร้าง token
        String token = jwtUtil.generateToken(reporter.getId() + ":" + UserRoleEnum.REPORTER.name());

        return buildReporterResponse(reporter, token);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDTO loginStaff(LoginRequestDTO dto) {
        // หา staff ด้วย email
        StaffEntity staff = staffRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        // เช็ค password
        if (!passwordEncoder.matches(dto.getPassword(), staff.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        // เช็คว่าถูก block หรือไม่
        if (Boolean.TRUE.equals(staff.getIsBlocked())) {
            throw new UnauthorizedException("Account is blocked");
        }

        // สร้าง token
        String token = jwtUtil.generateToken(staff.getId() + ":" + UserRoleEnum.STAFF.name());

        return buildStaffResponse(staff, token);
    }

    // ==================== PRIVATE HELPERS ====================

    // สร้าง LoginResponseDTO จาก ReporterEntity
    private LoginResponseDTO buildReporterResponse(ReporterEntity entity, String token) {
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setId(entity.getId());
        response.setEmail(entity.getEmail());
        response.setFullName(entity.getFullName());
        response.setPhone(entity.getPhone());
        response.setRole(UserRoleEnum.REPORTER.name());
        response.setIsBlocked(entity.getIsBlocked());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }

    // สร้าง LoginResponseDTO จาก StaffEntity
    private LoginResponseDTO buildStaffResponse(StaffEntity entity, String token) {
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setId(entity.getId());
        response.setEmail(entity.getEmail());
        response.setFullName(entity.getFullName());
        response.setPhone(entity.getPhone());
        response.setRole(UserRoleEnum.STAFF.name());
        response.setIsBlocked(entity.getIsBlocked());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}
