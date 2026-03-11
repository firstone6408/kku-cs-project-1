import { z } from "zod";

// ============================
// Custom Error — ใช้ตัวเดียว แยกด้วย type
// ============================

/** ประเภท error */
export type AppErrorType = "validation" | "api" | "network" | "unknown";

/** Custom Error สำหรับทั้งระบบ — แยกประเภทด้วย type */
export class AppError extends Error {
  type: AppErrorType;

  constructor(type: AppErrorType, message: string) {
    super(message);
    this.name = "AppError";
    this.type = type;
  }
}

// ============================
// Validate Helper
// ============================

/**
 * Validate ข้อมูลด้วย zod schema — ถ้าไม่ผ่าน throw AppError(type: "validation")
 */
export function validateSchema<T>(schema: z.ZodSchema<T>, data: unknown): T {
  const parsed = schema.safeParse(data);

  if (!parsed.success) {
    const firstMessage = parsed.error.issues[0]?.message || "ข้อมูลไม่ถูกต้อง";
    throw new AppError("validation", firstMessage);
  }

  return parsed.data;
}
