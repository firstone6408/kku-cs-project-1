package com.kku.emergency_alert_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kku.emergency_alert_api.constant.StaffRoleEnum;
import com.kku.emergency_alert_api.dto.auth.LoginRequestDTO;
import com.kku.emergency_alert_api.dto.auth.LoginResponseDTO;
import com.kku.emergency_alert_api.dto.auth.RegisterReporterRequestDTO;
import com.kku.emergency_alert_api.dto.auth.RegisterStaffRequestDTO;
import com.kku.emergency_alert_api.exception.UnauthorizedException;
import com.kku.emergency_alert_api.interceptor.AuthInterceptor;
import com.kku.emergency_alert_api.interceptor.RoleInterceptor;
import com.kku.emergency_alert_api.service.auth.contract.AuthService;
import com.kku.emergency_alert_api.util.JwtUtil;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthInterceptor authInterceptor;

    @MockitoBean
    private RoleInterceptor roleInterceptor;

    @MockitoBean
    private JwtUtil jwtUtil;

    private LoginResponseDTO mockLoginResponse;

    @BeforeEach
    void setUp() throws Exception {
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(roleInterceptor.preHandle(any(), any(), any())).thenReturn(true);

        mockLoginResponse = new LoginResponseDTO();
        mockLoginResponse.setToken("mock_token");
        mockLoginResponse.setId(1L);
        mockLoginResponse.setEmail("user@kku.ac.th");
        mockLoginResponse.setFullName("User KKU");
        mockLoginResponse.setPhone("0812345678");
        mockLoginResponse.setRole("REPORTER");
        mockLoginResponse.setIsBlocked(false);
        mockLoginResponse.setCreatedAt(LocalDateTime.now());
    }

    // ==================== POST /api/auth/reporter/register ====================
    @Nested
    @DisplayName("POST /api/auth/reporter/register")
    class RegisterReporterTests {

        @Test
        @DisplayName("สมัคร Reporter สำเร็จ — 200 + token")
        void register_success() throws Exception {
            RegisterReporterRequestDTO dto = new RegisterReporterRequestDTO();
            dto.setEmail("reporter@kku.ac.th");
            dto.setFullName("Reporter KKU");
            dto.setPhone("0812345678");
            dto.setPassword("password123");

            when(authService.registerReporter(any())).thenReturn(mockLoginResponse);

            mockMvc.perform(post("/api/auth/reporter/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.data.token").value("mock_token"))
                    .andExpect(jsonPath("$.data.email").value("user@kku.ac.th"));
        }

        @Test
        @DisplayName("สมัคร Reporter ไม่สำเร็จ — email ซ้ำ (409)")
        void register_duplicateEmail() throws Exception {
            RegisterReporterRequestDTO dto = new RegisterReporterRequestDTO();
            dto.setEmail("reporter@kku.ac.th");
            dto.setFullName("Reporter KKU");
            dto.setPhone("0812345678");
            dto.setPassword("password123");

            when(authService.registerReporter(any()))
                    .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists"));

            mockMvc.perform(post("/api/auth/reporter/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("สมัคร Reporter ไม่สำเร็จ — validation ไม่ผ่าน (400)")
        void register_validationFail() throws Exception {
            RegisterReporterRequestDTO dto = new RegisterReporterRequestDTO();
            dto.setEmail(""); // email ว่าง
            dto.setPassword("12345"); // สั้นเกินไป

            mockMvc.perform(post("/api/auth/reporter/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ==================== POST /api/auth/reporter/login ====================
    @Nested
    @DisplayName("POST /api/auth/reporter/login")
    class LoginReporterTests {

        @Test
        @DisplayName("Login Reporter สำเร็จ — 200 + token")
        void login_success() throws Exception {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("reporter@kku.ac.th");
            dto.setPassword("password123");

            when(authService.loginReporter(any())).thenReturn(mockLoginResponse);

            mockMvc.perform(post("/api/auth/reporter/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.data.token").value("mock_token"));
        }

        @Test
        @DisplayName("Login Reporter ไม่สำเร็จ — email/password ผิด (401)")
        void login_invalidCredentials() throws Exception {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("reporter@kku.ac.th");
            dto.setPassword("wrongpassword");

            when(authService.loginReporter(any()))
                    .thenThrow(new UnauthorizedException("Invalid email or password"));

            mockMvc.perform(post("/api/auth/reporter/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Login Reporter ไม่สำเร็จ — ถูก block (401)")
        void login_blocked() throws Exception {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("reporter@kku.ac.th");
            dto.setPassword("password123");

            when(authService.loginReporter(any()))
                    .thenThrow(new UnauthorizedException("Account is blocked"));

            mockMvc.perform(post("/api/auth/reporter/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ==================== POST /api/auth/staff/register ====================
    @Nested
    @DisplayName("POST /api/auth/staff/register")
    class RegisterStaffTests {

        @Test
        @DisplayName("สมัคร Staff สำเร็จ — 200 + token")
        void register_success() throws Exception {
            RegisterStaffRequestDTO dto = new RegisterStaffRequestDTO();
            dto.setEmail("staff@kku.ac.th");
            dto.setFullName("Staff KKU");
            dto.setPhone("0898765432");
            dto.setRole(StaffRoleEnum.VOLUNTEER);
            dto.setPassword("password123");

            mockLoginResponse.setRole("STAFF");
            when(authService.registerStaff(any())).thenReturn(mockLoginResponse);

            mockMvc.perform(post("/api/auth/staff/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.data.token").value("mock_token"));
        }

        @Test
        @DisplayName("สมัคร Staff ไม่สำเร็จ — ไม่ระบุ role (400)")
        void register_noRole() throws Exception {
            RegisterStaffRequestDTO dto = new RegisterStaffRequestDTO();
            dto.setEmail("staff@kku.ac.th");
            dto.setFullName("Staff KKU");
            dto.setPhone("0898765432");
            dto.setRole(null); // ไม่ระบุ role
            dto.setPassword("password123");

            mockMvc.perform(post("/api/auth/staff/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ==================== POST /api/auth/staff/login ====================
    @Nested
    @DisplayName("POST /api/auth/staff/login")
    class LoginStaffTests {

        @Test
        @DisplayName("Login Staff สำเร็จ — 200 + token")
        void login_success() throws Exception {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("staff@kku.ac.th");
            dto.setPassword("password123");

            mockLoginResponse.setRole("STAFF");
            when(authService.loginStaff(any())).thenReturn(mockLoginResponse);

            mockMvc.perform(post("/api/auth/staff/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.data.token").value("mock_token"));
        }

        @Test
        @DisplayName("Login Staff ไม่สำเร็จ — email/password ผิด (401)")
        void login_invalidCredentials() throws Exception {
            LoginRequestDTO dto = new LoginRequestDTO();
            dto.setEmail("staff@kku.ac.th");
            dto.setPassword("wrongpassword");

            when(authService.loginStaff(any()))
                    .thenThrow(new UnauthorizedException("Invalid email or password"));

            mockMvc.perform(post("/api/auth/staff/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isUnauthorized());
        }
    }
}
