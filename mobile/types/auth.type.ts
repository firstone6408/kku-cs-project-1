// ประเภท user ที่ได้จาก API (ใช้ร่วมทั้ง Reporter และ Staff)
export interface User {
  id: number;
  email: string;
  fullName: string;
  phone: string;
  role: "REPORTER" | "STAFF";
  isBlocked: boolean;
  createdAt: string;
}

// response จาก login/register API
export interface LoginResponse {
  token: string;
  id: number;
  email: string;
  fullName: string;
  phone: string;
  role: "REPORTER" | "STAFF";
  isBlocked: boolean;
  createdAt: string;
}

// request สำหรับ login (ใช้ร่วมทั้ง Reporter และ Staff)
export interface LoginRequest {
  email: string;
  password: string;
}

// request สำหรับสมัคร Reporter
export interface RegisterReporterRequest {
  email: string;
  fullName: string;
  phone: string;
  password: string;
}

// request สำหรับสมัคร Staff
export interface RegisterStaffRequest {
  email: string;
  fullName: string;
  phone: string;
  role: "VOLUNTEER" | "OFFICER";
  password: string;
}
