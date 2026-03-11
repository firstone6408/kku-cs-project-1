// ค่า config สำหรับเรียก API (ใช้ EXPO_PUBLIC_API_URL จาก .env)
export const API_CONFIG = {
  BASE_URL: process.env.EXPO_PUBLIC_API_URL || "http://10.51.23.130:8080",
  ENDPOINTS: {
    AUTH: {
      REPORTER_REGISTER: "/api/auth/reporter/register",
      REPORTER_LOGIN: "/api/auth/reporter/login",
      STAFF_REGISTER: "/api/auth/staff/register",
      STAFF_LOGIN: "/api/auth/staff/login",
    },
  },
} as const;
