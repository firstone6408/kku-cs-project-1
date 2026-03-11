// ============================
// ค่าคงที่สำหรับ Role
// ============================

/** Role หลักของ user (Reporter / Staff) */
export type UserRole = "REPORTER" | "STAFF";

/** Role ย่อยของ Staff (อาสาสมัคร / เจ้าหน้าที่) */
export type StaffRole = "VOLUNTEER" | "OFFICER";

/** ค่า enum สำหรับใช้ใน runtime (เช่น loop, comparison) */
export const USER_ROLES = {
  REPORTER: "REPORTER",
  STAFF: "STAFF",
} as const;

export const STAFF_ROLES = {
  VOLUNTEER: "VOLUNTEER",
  OFFICER: "OFFICER",
} as const;

// ============================
// Types
// ============================

/** ข้อมูล user ที่ได้จาก API (ใช้ร่วมทั้ง Reporter และ Staff) */
export interface User {
  id: number;
  email: string;
  fullName: string;
  phone: string;
  role: UserRole;
  isBlocked: boolean;
  createdAt: string;
}

/** Response จาก login/register API */
export interface LoginResponse {
  token: string;
  id: number;
  email: string;
  fullName: string;
  phone: string;
  role: UserRole;
  isBlocked: boolean;
  createdAt: string;
}

/** Request สำหรับ login (ใช้ร่วมทั้ง Reporter และ Staff) */
export interface LoginRequest {
  email: string;
  password: string;
}

/** Request สำหรับสมัคร Reporter */
export interface RegisterReporterRequest {
  email: string;
  fullName: string;
  phone: string;
  password: string;
}

/** Request สำหรับสมัคร Staff */
export interface RegisterStaffRequest {
  email: string;
  fullName: string;
  phone: string;
  role: StaffRole;
  password: string;
}
