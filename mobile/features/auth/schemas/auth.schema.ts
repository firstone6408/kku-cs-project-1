import { z } from "zod";
import { STAFF_ROLES } from "@/types/auth.type";

// schema สำหรับ validate form login
export const loginSchema = z.object({
  email: z.string().min(1, "กรุณากรอก email").email("รูปแบบ email ไม่ถูกต้อง"),
  password: z.string().min(6, "รหัสผ่านต้องมีอย่างน้อย 6 ตัวอักษร"),
});

// schema สำหรับ validate form สมัคร Reporter
export const registerReporterSchema = z.object({
  email: z.string().min(1, "กรุณากรอก email").email("รูปแบบ email ไม่ถูกต้อง"),
  fullName: z.string().min(1, "กรุณากรอกชื่อ-สกุล"),
  phone: z.string().min(1, "กรุณากรอกเบอร์โทร"),
  password: z.string().min(6, "รหัสผ่านต้องมีอย่างน้อย 6 ตัวอักษร"),
});

// schema สำหรับ validate form สมัคร Staff
export const registerStaffSchema = z.object({
  email: z.string().min(1, "กรุณากรอก email").email("รูปแบบ email ไม่ถูกต้อง"),
  fullName: z.string().min(1, "กรุณากรอกชื่อ-สกุล"),
  phone: z.string().min(1, "กรุณากรอกเบอร์โทร"),
  role: z.enum([STAFF_ROLES.VOLUNTEER, STAFF_ROLES.OFFICER], {
    error: "กรุณาเลือกบทบาท",
  }),
  password: z.string().min(6, "รหัสผ่านต้องมีอย่างน้อย 6 ตัวอักษร"),
});

// infer type จาก schema (ไม่ต้องเขียน type ซ้ำ)
export type LoginInput = z.infer<typeof loginSchema>;
export type RegisterReporterInput = z.infer<typeof registerReporterSchema>;
export type RegisterStaffInput = z.infer<typeof registerStaffSchema>;
