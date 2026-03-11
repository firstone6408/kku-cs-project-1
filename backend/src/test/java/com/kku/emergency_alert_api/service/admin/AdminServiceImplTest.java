package com.kku.emergency_alert_api.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

import com.kku.emergency_alert_api.dto.admin.AdminRequestDTO;
import com.kku.emergency_alert_api.dto.admin.AdminResponseDTO;
import com.kku.emergency_alert_api.dto.admin.AdminUpdateRequestDTO;
import com.kku.emergency_alert_api.entity.AdminEntity;
import com.kku.emergency_alert_api.exception.ResourceNotFoundException;
import com.kku.emergency_alert_api.repository.AdminRepository;
import com.kku.emergency_alert_api.service.admin.impl.AdminServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdminServiceImpl Tests")
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    private AdminEntity mockAdmin;
    private AdminRequestDTO createRequest;
    private AdminUpdateRequestDTO updateRequest;

    @BeforeEach
    void setUp() {
        // สร้างข้อมูล mock สำหรับทดสอบ
        mockAdmin = new AdminEntity();
        mockAdmin.setId(1L);
        mockAdmin.setEmail("admin@kku.ac.th");
        mockAdmin.setFullName("Admin KKU");
        mockAdmin.setPasswordHash("hashed_password");
        mockAdmin.setCreatedAt(LocalDateTime.now());

        createRequest = new AdminRequestDTO();
        createRequest.setEmail("admin@kku.ac.th");
        createRequest.setFullName("Admin KKU");
        createRequest.setPassword("password123");

        updateRequest = new AdminUpdateRequestDTO();
    }

    // ==================== CREATE ====================
    @Nested
    @DisplayName("create()")
    class CreateTests {

        @Test
        @DisplayName("สร้าง admin สำเร็จ — ข้อมูลถูกต้อง")
        void create_success() {
            when(adminRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
            when(adminRepository.save(any(AdminEntity.class))).thenReturn(mockAdmin);

            AdminResponseDTO result = adminService.create(createRequest);

            assertNotNull(result);
            assertEquals("admin@kku.ac.th", result.getEmail());
            assertEquals("Admin KKU", result.getFullName());
            verify(adminRepository, times(1)).save(any(AdminEntity.class));
            verify(passwordEncoder, times(1)).encode("password123");
        }

        @Test
        @DisplayName("สร้าง admin ไม่สำเร็จ — email ซ้ำ (409 CONFLICT)")
        void create_duplicateEmail_throwsConflict() {
            when(adminRepository.existsByEmail("admin@kku.ac.th")).thenReturn(true);

            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class,
                    () -> adminService.create(createRequest));

            assertEquals(409, exception.getStatusCode().value());
            verify(adminRepository, never()).save(any(AdminEntity.class));
        }
    }

    // ==================== GET ALL ====================
    @Nested
    @DisplayName("getAll()")
    class GetAllTests {

        @Test
        @DisplayName("ดึง admin ทั้งหมด — มีข้อมูล")
        void getAll_hasData() {
            AdminEntity admin2 = new AdminEntity();
            admin2.setId(2L);
            admin2.setEmail("admin2@kku.ac.th");
            admin2.setFullName("Admin 2");
            admin2.setPasswordHash("hashed");
            admin2.setCreatedAt(LocalDateTime.now());

            when(adminRepository.findAll()).thenReturn(Arrays.asList(mockAdmin, admin2));

            List<AdminResponseDTO> result = adminService.getAll();

            assertEquals(2, result.size());
            assertEquals("admin@kku.ac.th", result.get(0).getEmail());
            assertEquals("admin2@kku.ac.th", result.get(1).getEmail());
        }

        @Test
        @DisplayName("ดึง admin ทั้งหมด — ไม่มีข้อมูล (return empty list)")
        void getAll_empty() {
            when(adminRepository.findAll()).thenReturn(List.of());

            List<AdminResponseDTO> result = adminService.getAll();

            assertEquals(0, result.size());
        }
    }

    // ==================== GET BY ID ====================
    @Nested
    @DisplayName("getById()")
    class GetByIdTests {

        @Test
        @DisplayName("ดึง admin ตาม ID — พบข้อมูล")
        void getById_found() {
            when(adminRepository.findById(1L)).thenReturn(Optional.of(mockAdmin));

            AdminResponseDTO result = adminService.getById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("admin@kku.ac.th", result.getEmail());
        }

        @Test
        @DisplayName("ดึง admin ตาม ID — ไม่พบ (404 NOT FOUND)")
        void getById_notFound() {
            when(adminRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> adminService.getById(999L));
        }
    }

    // ==================== UPDATE ====================
    @Nested
    @DisplayName("update()")
    class UpdateTests {

        @Test
        @DisplayName("แก้ไข admin สำเร็จ — แก้ทุก field")
        void update_allFields_success() {
            updateRequest.setEmail("new@kku.ac.th");
            updateRequest.setFullName("New Name");
            updateRequest.setPassword("newpassword");

            when(adminRepository.findById(1L)).thenReturn(Optional.of(mockAdmin));
            when(adminRepository.existsByEmail("new@kku.ac.th")).thenReturn(false);
            when(passwordEncoder.encode("newpassword")).thenReturn("new_hashed");
            when(adminRepository.save(any(AdminEntity.class))).thenReturn(mockAdmin);

            AdminResponseDTO result = adminService.update(1L, updateRequest);

            assertNotNull(result);
            verify(passwordEncoder, times(1)).encode("newpassword");
            verify(adminRepository, times(1)).save(any(AdminEntity.class));
        }

        @Test
        @DisplayName("แก้ไข admin สำเร็จ — แก้แค่ชื่อ (partial update)")
        void update_partialUpdate_onlyFullName() {
            updateRequest.setFullName("Updated Name");

            when(adminRepository.findById(1L)).thenReturn(Optional.of(mockAdmin));
            when(adminRepository.save(any(AdminEntity.class))).thenReturn(mockAdmin);

            AdminResponseDTO result = adminService.update(1L, updateRequest);

            assertNotNull(result);
            verify(passwordEncoder, never()).encode(anyString());
            verify(adminRepository, never()).existsByEmail(anyString());
        }

        @Test
        @DisplayName("แก้ไข admin ไม่สำเร็จ — ไม่พบ admin (404)")
        void update_notFound() {
            when(adminRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> adminService.update(999L, updateRequest));
            verify(adminRepository, never()).save(any(AdminEntity.class));
        }

        @Test
        @DisplayName("แก้ไข admin ไม่สำเร็จ — email ซ้ำกับคนอื่น (409)")
        void update_duplicateEmail_throwsConflict() {
            updateRequest.setEmail("existing@kku.ac.th");

            when(adminRepository.findById(1L)).thenReturn(Optional.of(mockAdmin));
            when(adminRepository.existsByEmail("existing@kku.ac.th")).thenReturn(true);

            assertThrows(ResponseStatusException.class,
                    () -> adminService.update(1L, updateRequest));
            verify(adminRepository, never()).save(any(AdminEntity.class));
        }

        @Test
        @DisplayName("แก้ไข admin สำเร็จ — email เดิมของตัวเอง (ไม่ถือว่าซ้ำ)")
        void update_sameEmail_noConflict() {
            updateRequest.setEmail("admin@kku.ac.th"); // email เดิมของตัวเอง

            when(adminRepository.findById(1L)).thenReturn(Optional.of(mockAdmin));
            when(adminRepository.save(any(AdminEntity.class))).thenReturn(mockAdmin);

            AdminResponseDTO result = adminService.update(1L, updateRequest);

            assertNotNull(result);
            // ไม่ต้องเช็ค existsByEmail เพราะ email เดิมของตัวเอง
            verify(adminRepository, times(1)).save(any(AdminEntity.class));
        }
    }

    // ==================== DELETE ====================
    @Nested
    @DisplayName("delete()")
    class DeleteTests {

        @Test
        @DisplayName("ลบ admin สำเร็จ")
        void delete_success() {
            when(adminRepository.existsById(1L)).thenReturn(true);

            adminService.delete(1L);

            verify(adminRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("ลบ admin ไม่สำเร็จ — ไม่พบ (404)")
        void delete_notFound() {
            when(adminRepository.existsById(999L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class,
                    () -> adminService.delete(999L));
            verify(adminRepository, never()).deleteById(anyLong());
        }
    }
}
