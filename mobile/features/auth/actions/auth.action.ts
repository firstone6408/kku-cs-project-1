import { useAuth } from "@/store/auth.store";
import { User, UserRole, StaffRole } from "@/types/auth.type";
import { validateSchema, AppError } from "@/utils/validate.utils";
import {
  loginReporter,
  loginStaff,
  registerReporter,
  registerStaff,
} from "../api/auth.api";
import {
  loginSchema,
  registerReporterSchema,
  registerStaffSchema,
} from "../schemas/auth.schema";

// ============================
// Types — raw data จาก form (ยังไม่ validate)
// ============================

/** ข้อมูลดิบจาก login form */
export type LoginFormData = {
  email: string;
  password: string;
};

/** ข้อมูลดิบจาก register form (Reporter) */
export type RegisterReporterFormData = {
  email: string;
  fullName: string;
  phone: string;
  password: string;
};

/** ข้อมูลดิบจาก register form (Staff) */
export type RegisterStaffFormData = {
  email: string;
  fullName: string;
  phone: string;
  role: StaffRole;
  password: string;
};

// ============================
// Helpers
// ============================

/** แปลง API response → User + เก็บลง store */
function handleAuthResult(result: { data: Record<string, unknown> }) {
  const data = result.data;

  const user: User = {
    id: data.id as number,
    email: data.email as string,
    fullName: data.fullName as string,
    phone: data.phone as string,
    role: data.role as UserRole,
    isBlocked: data.isBlocked as boolean,
    createdAt: data.createdAt as string,
  };

  useAuth.getState().setUser(user);
  useAuth.getState().setToken(data.token as string);
}

/** ตรวจ error จาก API แล้ว throw ApiError */
function checkError(error: { status: string; errorMessage: string }) {
  if (error.status === "error") {
    throw new AppError("api", error.errorMessage);
  }
}

// ============================
// Actions — validate → API → store
// ============================

/** Login ผู้แจ้งเหตุ (Reporter) */
export async function loginReporterAction(
  formData: LoginFormData,
): Promise<void> {
  const data = validateSchema(loginSchema, formData);
  const { result, error } = await loginReporter(data);
  checkError(error);
  handleAuthResult(result);
}

/** Login พนักงาน (Staff) */
export async function loginStaffAction(formData: LoginFormData): Promise<void> {
  const data = validateSchema(loginSchema, formData);
  const { result, error } = await loginStaff(data);
  checkError(error);
  handleAuthResult(result);
}

/** สมัครผู้แจ้งเหตุ (Reporter) — validate + API + auto-login */
export async function registerReporterAction(
  formData: RegisterReporterFormData,
): Promise<void> {
  const data = validateSchema(registerReporterSchema, formData);
  const { result, error } = await registerReporter(data);
  checkError(error);
  handleAuthResult(result);
}

/** สมัครพนักงาน (Staff) — validate + API + auto-login */
export async function registerStaffAction(
  formData: RegisterStaffFormData,
): Promise<void> {
  const data = validateSchema(registerStaffSchema, formData);
  const { result, error } = await registerStaff(data);
  checkError(error);
  handleAuthResult(result);
}
