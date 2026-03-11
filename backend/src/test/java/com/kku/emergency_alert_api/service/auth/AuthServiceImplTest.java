package com.kku.emergency_alert_api.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.kku.emergency_alert_api.constant.StaffRoleEnum;
import com.kku.emergency_alert_api.dto.auth.LoginRequestDTO;
import com.kku.emergency_alert_api.dto.auth.LoginResponseDTO;
import com.kku.emergency_alert_api.dto.auth.RegisterReporterRequestDTO;
import com.kku.emergency_alert_api.dto.auth.RegisterStaffRequestDTO;
import com.kku.emergency_alert_api.entity.ReporterEntity;
import com.kku.emergency_alert_api.entity.StaffEntity;
import com.kku.emergency_alert_api.exception.UnauthorizedException;
import com.kku.emergency_alert_api.repository.ReporterRepository;
import com.kku.emergency_alert_api.repository.StaffRepository;
import com.kku.emergency_alert_api.service.auth.impl.AuthServiceImpl;
import com.kku.emergency_alert_api.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl Tests")
class AuthServiceImplTest {

    @Mock
    private ReporterRepository reporterRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private ReporterEntity mockReporter;
    private StaffEntity mockStaff;

    @BeforeEach
    void setUp() {
        mockReporter = new ReporterEntity();
        mockReporter.setId(1L);
        mockReporter.setEmail("reporter@kku.ac.th");
        mockReporter.setFullName("Reporter KKU");
        mockReporter.setPhone("0812345678");
        mockReporter.setPasswordHash("hashed_password");
        mockReporter.setIsBlocked(false);
        mockReporter.setCreatedAt(LocalDateTime.now());

        mockStaff = new StaffEntity();
        mockStaff.setId(1L);
        mockStaff.setEmail("staff@kku.ac.th");
        mockStaff.setFullName("Staff KKU");
        mockStaff.setPhone("0898765432");
        mockStaff.setRole(StaffRoleEnum.VOLUNTEER);
        mockStaff.setPasswordHash("hashed_password");
        mockStaff.setIsBlocked(false);
        mockStaff.setCreatedAt(LocalDateTime.now());
    }

    // ==================== REGISTER REPORTER ====================
    @Nested
    @DisplayName("registerReporter()")
    class RegisterReporterTests {

        @Test
        @DisplayName("สมัคร Reporter สำเร็จ — ได้ token กลับมา (auto-login)")
        void registerReporter_success() {
            RegisterReporterRequestDTO dto = new RegisterReporterRequestDTO();
            dto.setEmail("reporter@kku.ac.th");
            dto.setFullName("Reporter KKU");
            dto.setPhone("0812345678");
            dto.setPassword("password123");

            when(reporterRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
            when(reporterRepository.save(any(ReporterEntity.class))).thenReturn(mockReporter);
            when(jwtUtil.generateToken(anyString())).thenReturn("mock_token");

            LoginResponseDTO result = authService.registerReporter(dto);

            assertNotNull(result);
            assertEquals("mock_token", result.getToken());
            assertEquals("reporter@kku.ac.th", result.getEmail());
            assertEquals("REPORTER", result.getRole());
        }

        @Test
        @DisplayName("สมัคร Reporter ไม่สำเร็จ — email ซ้ำ (409)")
        void registerReporter_duplicateEmail() {
            RegisterReporterRequestDTO dto = new RegisterReporterRequestDTO();
            dto.setEmail("reporter@kku.ac.th");

            when(reporterRepository.existsByEmail("reporter@kku.ac.th")).thenReturn(true);

            assertThrows(ResponseStatusException.class, () -> authService.registerReporter(dto));
            verify(reporterRepository, never()).save(any());
        }
    }

    // ==================== REGISTER STAFF ====================
    @Nested
    @DisplayName("registerStaff()")
    class RegisterStaffTests {

        @Test
        @DisplayName("สมัคร Staff สำเร็จ — ได้ token กลับมา (auto-login)")
        void registerStaff_success() {
            RegisterStaffRequestDTO dto = new RegisterStaffRequestDTO();
            dto.setEmail("staff@kku.ac.th");
            dto.setFullName("Staff KKU");
            dto.setPhone("0898765432");
            dto.setRole(StaffRoleEnum.VOLUNTEER);
            dto.setPassword("password123");

            when(staffRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
            when(staffRepository.save(any(StaffEntity.class))).thenReturn(mockStaff);
            when(jwtUtil.generateToken(anyString())).thenReturn("mock_token");

            LoginResponseDTO result = authService.registerStaff(dto);

            assertNotNull(result);
            assertEquals("mock_token", result.getToken());
            assertEquals("STAFF", result.getRole());
        }

        @Test
        @DisplayName("สมัคร Staff ไม่สำเร็จ — email ซ้ำ (409)")
        void registerStaff_duplicateEmail() {
            RegisterStaffRequestDTO dto = new RegisterStaffRequestDTO();
            dto.setEmail("staff@kku.ac.th");

            when(staffRepository.existsByEmail("staff@kku.ac.th")).thenReturn(true);

            assertThrows(ResponseStatusException.class, () -> authService.registerStaff(dto));
            verify(staffRepository, never()).save(any());
        }
    }

    // ==================== LOGIN REPORTER ====================
    @Nested
    @DisplayName("loginReporter()")
    class LoginReporterTests {

        @Test
        @DisplayName("Login Reporter สำเร็จ")
        void loginReporter_success() {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("reporter@kku.ac.th");
            dto.setPassword("password123");

            when(reporterRepository.findByEmail("reporter@kku.ac.th")).thenReturn(Optional.of(mockReporter));
            when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);
            when(jwtUtil.generateToken(anyString())).thenReturn("mock_token");

            LoginResponseDTO result = authService.loginReporter(dto);

            assertNotNull(result);
            assertEquals("mock_token", result.getToken());
            assertEquals("REPORTER", result.getRole());
        }

        @Test
        @DisplayName("Login Reporter ไม่สำเร็จ — ไม่พบ email")
        void loginReporter_emailNotFound() {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("notfound@kku.ac.th");
            dto.setPassword("password123");

            when(reporterRepository.findByEmail("notfound@kku.ac.th")).thenReturn(Optional.empty());

            assertThrows(UnauthorizedException.class, () -> authService.loginReporter(dto));
        }

        @Test
        @DisplayName("Login Reporter ไม่สำเร็จ — password ผิด")
        void loginReporter_wrongPassword() {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("reporter@kku.ac.th");
            dto.setPassword("wrongpassword");

            when(reporterRepository.findByEmail("reporter@kku.ac.th")).thenReturn(Optional.of(mockReporter));
            when(passwordEncoder.matches("wrongpassword", "hashed_password")).thenReturn(false);

            assertThrows(UnauthorizedException.class, () -> authService.loginReporter(dto));
        }

        @Test
        @DisplayName("Login Reporter ไม่สำเร็จ — ถูก block")
        void loginReporter_blocked() {
            mockReporter.setIsBlocked(true);

            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("reporter@kku.ac.th");
            dto.setPassword("password123");

            when(reporterRepository.findByEmail("reporter@kku.ac.th")).thenReturn(Optional.of(mockReporter));
            when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);

            assertThrows(UnauthorizedException.class, () -> authService.loginReporter(dto));
        }
    }

    // ==================== LOGIN STAFF ====================
    @Nested
    @DisplayName("loginStaff()")
    class LoginStaffTests {

        @Test
        @DisplayName("Login Staff สำเร็จ")
        void loginStaff_success() {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("staff@kku.ac.th");
            dto.setPassword("password123");

            when(staffRepository.findByEmail("staff@kku.ac.th")).thenReturn(Optional.of(mockStaff));
            when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);
            when(jwtUtil.generateToken(anyString())).thenReturn("mock_token");

            LoginResponseDTO result = authService.loginStaff(dto);

            assertNotNull(result);
            assertEquals("mock_token", result.getToken());
            assertEquals("STAFF", result.getRole());
        }

        @Test
        @DisplayName("Login Staff ไม่สำเร็จ — ไม่พบ email")
        void loginStaff_emailNotFound() {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("notfound@kku.ac.th");
            dto.setPassword("password123");

            when(staffRepository.findByEmail("notfound@kku.ac.th")).thenReturn(Optional.empty());

            assertThrows(UnauthorizedException.class, () -> authService.loginStaff(dto));
        }

        @Test
        @DisplayName("Login Staff ไม่สำเร็จ — password ผิด")
        void loginStaff_wrongPassword() {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("staff@kku.ac.th");
            dto.setPassword("wrongpassword");

            when(staffRepository.findByEmail("staff@kku.ac.th")).thenReturn(Optional.of(mockStaff));
            when(passwordEncoder.matches("wrongpassword", "hashed_password")).thenReturn(false);

            assertThrows(UnauthorizedException.class, () -> authService.loginStaff(dto));
        }

        @Test
        @DisplayName("Login Staff ไม่สำเร็จ — ถูก block")
        void loginStaff_blocked() {
            mockStaff.setIsBlocked(true);

            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("staff@kku.ac.th");
            dto.setPassword("password123");

            when(staffRepository.findByEmail("staff@kku.ac.th")).thenReturn(Optional.of(mockStaff));
            when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);

            assertThrows(UnauthorizedException.class, () -> authService.loginStaff(dto));
        }
    }
}
