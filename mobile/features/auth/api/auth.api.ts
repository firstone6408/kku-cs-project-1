import { API_CONFIG } from "@/configs/api.config";
import { RegisterReporterRequest, RegisterStaffRequest, LoginRequest } from "@/types/auth.type";
import { templateValidateResponse, withApiHandling } from "@/utils/api.utils";
import { z } from "zod";

// schema สำหรับ validate response จาก server
const loginResponseSchema = templateValidateResponse(
  z.object({
    token: z.string(),
    id: z.number(),
    email: z.string(),
    fullName: z.string(),
    phone: z.string(),
    role: z.string(),
    isBlocked: z.boolean(),
    createdAt: z.string(),
  })
);

// สมัครผู้แจ้งเหตุ (Reporter)
export async function registerReporter(data: RegisterReporterRequest) {
  return withApiHandling<z.infer<typeof loginResponseSchema>>(
    fetch(API_CONFIG.BASE_URL + API_CONFIG.ENDPOINTS.AUTH.REPORTER_REGISTER, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    }),
    { validateResponse: loginResponseSchema }
  );
}

// สมัครพนักงาน (Staff)
export async function registerStaff(data: RegisterStaffRequest) {
  return withApiHandling<z.infer<typeof loginResponseSchema>>(
    fetch(API_CONFIG.BASE_URL + API_CONFIG.ENDPOINTS.AUTH.STAFF_REGISTER, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    }),
    { validateResponse: loginResponseSchema }
  );
}

// เข้าสู่ระบบผู้แจ้งเหตุ (Reporter)
export async function loginReporter(data: LoginRequest) {
  return withApiHandling<z.infer<typeof loginResponseSchema>>(
    fetch(API_CONFIG.BASE_URL + API_CONFIG.ENDPOINTS.AUTH.REPORTER_LOGIN, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    }),
    { validateResponse: loginResponseSchema }
  );
}

// เข้าสู่ระบบพนักง (Staff)
export async function loginStaff(data: LoginRequest) {
  return withApiHandling<z.infer<typeof loginResponseSchema>>(
    fetch(API_CONFIG.BASE_URL + API_CONFIG.ENDPOINTS.AUTH.STAFF_LOGIN, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    }),
    { validateResponse: loginResponseSchema }
  );
}
