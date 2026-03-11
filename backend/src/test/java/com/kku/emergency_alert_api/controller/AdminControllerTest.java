package com.kku.emergency_alert_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
import com.kku.emergency_alert_api.dto.admin.AdminRequestDTO;
import com.kku.emergency_alert_api.dto.admin.AdminResponseDTO;
import com.kku.emergency_alert_api.dto.admin.AdminUpdateRequestDTO;
import com.kku.emergency_alert_api.exception.ResourceNotFoundException;
import com.kku.emergency_alert_api.interceptor.AuthInterceptor;
import com.kku.emergency_alert_api.interceptor.RoleInterceptor;
import com.kku.emergency_alert_api.service.admin.contract.AdminService;
import com.kku.emergency_alert_api.util.JwtUtil;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AdminController Tests")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminService adminService;

    // Mock interceptor dependencies เพื่อไม่ให้ตรวจ JWT ตอนทดสอบ
    @MockitoBean
    private AuthInterceptor authInterceptor;

    @MockitoBean
    private RoleInterceptor roleInterceptor;

    @MockitoBean
    private JwtUtil jwtUtil;

    private AdminResponseDTO mockResponse;
    private AdminRequestDTO createRequest;
    private AdminUpdateRequestDTO updateRequest;

    @BeforeEach
    void setUp() throws Exception {
        // ให้ interceptor ผ่านหมดตอน test
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(roleInterceptor.preHandle(any(), any(), any())).thenReturn(true);

        mockResponse = new AdminResponseDTO();
        mockResponse.setId(1L);
        mockResponse.setEmail("admin@kku.ac.th");
        mockResponse.setFullName("Admin KKU");
        mockResponse.setCreatedAt(LocalDateTime.now());

        createRequest = new AdminRequestDTO();
        createRequest.setEmail("admin@kku.ac.th");
        createRequest.setFullName("Admin KKU");
        createRequest.setPassword("password123");

        updateRequest = new AdminUpdateRequestDTO();
    }

    // ==================== POST /api/admin/create ====================
    @Nested
    @DisplayName("POST /api/admin/create")
    class CreateEndpointTests {

        @Test
        @DisplayName("สร้าง admin สำเร็จ — 200 OK")
        void create_success() throws Exception {
            when(adminService.create(any(AdminRequestDTO.class))).thenReturn(mockResponse);

            mockMvc.perform(post("/api/admin/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.message").value("Admin created"))
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.email").value("admin@kku.ac.th"))
                    .andExpect(jsonPath("$.data.fullName").value("Admin KKU"));
        }

        @Test
        @DisplayName("สร้าง admin ไม่สำเร็จ — email ว่าง (400 BAD REQUEST)")
        void create_emptyEmail_validationFail() throws Exception {
            createRequest.setEmail("");

            mockMvc.perform(post("/api/admin/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("สร้าง admin ไม่สำเร็จ — ไม่มี password (400 BAD REQUEST)")
        void create_noPassword_validationFail() throws Exception {
            createRequest.setPassword(null);

            mockMvc.perform(post("/api/admin/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("สร้าง admin ไม่สำเร็จ — email ซ้ำ (409 CONFLICT)")
        void create_duplicateEmail() throws Exception {
            when(adminService.create(any(AdminRequestDTO.class)))
                    .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists"));

            mockMvc.perform(post("/api/admin/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.ok").value(false));
        }

        @Test
        @DisplayName("สร้าง admin ไม่สำเร็จ — email format ไม่ถูกต้อง (400)")
        void create_invalidEmailFormat() throws Exception {
            createRequest.setEmail("not-an-email");

            mockMvc.perform(post("/api/admin/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("สร้าง admin ไม่สำเร็จ — password สั้นเกินไป (400)")
        void create_shortPassword() throws Exception {
            createRequest.setPassword("12345"); // น้อยกว่า 6 ตัว

            mockMvc.perform(post("/api/admin/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ==================== GET /api/admin ====================
    @Nested
    @DisplayName("GET /api/admin")
    class GetAllEndpointTests {

        @Test
        @DisplayName("ดึง admin ทั้งหมด — มีข้อมูล")
        void getAll_hasData() throws Exception {
            AdminResponseDTO admin2 = new AdminResponseDTO();
            admin2.setId(2L);
            admin2.setEmail("admin2@kku.ac.th");
            admin2.setFullName("Admin 2");
            admin2.setCreatedAt(LocalDateTime.now());

            when(adminService.getAll()).thenReturn(Arrays.asList(mockResponse, admin2));

            mockMvc.perform(get("/api/admin"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].email").value("admin@kku.ac.th"))
                    .andExpect(jsonPath("$.data[1].email").value("admin2@kku.ac.th"));
        }

        @Test
        @DisplayName("ดึง admin ทั้งหมด — ไม่มีข้อมูล")
        void getAll_empty() throws Exception {
            when(adminService.getAll()).thenReturn(List.of());

            mockMvc.perform(get("/api/admin"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(0));
        }
    }

    // ==================== GET /api/admin/{id} ====================
    @Nested
    @DisplayName("GET /api/admin/{id}")
    class GetByIdEndpointTests {

        @Test
        @DisplayName("ดึง admin ตาม ID — พบข้อมูล")
        void getById_found() throws Exception {
            when(adminService.getById(1L)).thenReturn(mockResponse);

            mockMvc.perform(get("/api/admin/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.email").value("admin@kku.ac.th"));
        }

        @Test
        @DisplayName("ดึง admin ตาม ID — ไม่พบ (404)")
        void getById_notFound() throws Exception {
            when(adminService.getById(999L))
                    .thenThrow(new ResourceNotFoundException("Admin not found with id: 999"));

            mockMvc.perform(get("/api/admin/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.ok").value(false));
        }
    }

    // ==================== PUT /api/admin/{id} ====================
    @Nested
    @DisplayName("PUT /api/admin/{id}")
    class UpdateEndpointTests {

        @Test
        @DisplayName("แก้ไข admin สำเร็จ")
        void update_success() throws Exception {
            updateRequest.setFullName("Updated Name");

            AdminResponseDTO updatedResponse = new AdminResponseDTO();
            updatedResponse.setId(1L);
            updatedResponse.setEmail("admin@kku.ac.th");
            updatedResponse.setFullName("Updated Name");
            updatedResponse.setCreatedAt(LocalDateTime.now());

            when(adminService.update(eq(1L), any(AdminUpdateRequestDTO.class))).thenReturn(updatedResponse);

            mockMvc.perform(put("/api/admin/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.message").value("Admin updated"))
                    .andExpect(jsonPath("$.data.fullName").value("Updated Name"));
        }

        @Test
        @DisplayName("แก้ไข admin ไม่สำเร็จ — ไม่พบ (404)")
        void update_notFound() throws Exception {
            updateRequest.setFullName("Updated Name");

            when(adminService.update(eq(999L), any(AdminUpdateRequestDTO.class)))
                    .thenThrow(new ResourceNotFoundException("Admin not found with id: 999"));

            mockMvc.perform(put("/api/admin/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.ok").value(false));
        }

        @Test
        @DisplayName("แก้ไข admin ไม่สำเร็จ — email format ไม่ถูก (400)")
        void update_invalidEmail() throws Exception {
            updateRequest.setEmail("not-an-email");

            mockMvc.perform(put("/api/admin/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ==================== DELETE /api/admin/{id} ====================
    @Nested
    @DisplayName("DELETE /api/admin/{id}")
    class DeleteEndpointTests {

        @Test
        @DisplayName("ลบ admin สำเร็จ")
        void delete_success() throws Exception {
            doNothing().when(adminService).delete(1L);

            mockMvc.perform(delete("/api/admin/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.message").value("Admin deleted"));
        }

        @Test
        @DisplayName("ลบ admin ไม่สำเร็จ — ไม่พบ (404)")
        void delete_notFound() throws Exception {
            doThrow(new ResourceNotFoundException("Admin not found with id: 999"))
                    .when(adminService).delete(999L);

            mockMvc.perform(delete("/api/admin/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.ok").value(false));
        }
    }
}
