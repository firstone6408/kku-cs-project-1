import { useAuth } from "@/store/auth.store";
import { User } from "@/types/auth.type";
import { loginReporter, loginStaff, registerReporter, registerStaff } from "../api/auth.api";
import { LoginInput, RegisterReporterInput, RegisterStaffInput } from "../schemas/auth.schema";

// ใช้ร่วมกับ useMutation — รับข้อมูลจาก form แล้วเรียก API + เก็บ user ลง store

// Login Reporter — เรียก API แล้วเก็บ token + user เข้า store
export async function loginReporterAction(data: LoginInput): Promise<void> {
  const { result, error } = await loginReporter(data);

  if (error.status === "error") {
    throw new Error(error.errorMessage);
  }

  // เก็บ user + token ลง zustand store (AsyncStorage)
  const userData: User = {
    id: result.data.id,
    email: result.data.email,
    fullName: result.data.fullName,
    phone: result.data.phone,
    role: result.data.role as "REPORTER" | "STAFF",
    isBlocked: result.data.isBlocked,
    createdAt: result.data.createdAt,
  };

  useAuth.getState().setUser(userData);
  useAuth.getState().setToken(result.data.token);
}

// Login Staff — เรียก API แล้วเก็บ token + user เข้า store
export async function loginStaffAction(data: LoginInput): Promise<void> {
  const { result, error } = await loginStaff(data);

  if (error.status === "error") {
    throw new Error(error.errorMessage);
  }

  const userData: User = {
    id: result.data.id,
    email: result.data.email,
    fullName: result.data.fullName,
    phone: result.data.phone,
    role: result.data.role as "REPORTER" | "STAFF",
    isBlocked: result.data.isBlocked,
    createdAt: result.data.createdAt,
  };

  useAuth.getState().setUser(userData);
  useAuth.getState().setToken(result.data.token);
}

// Register Reporter — สมัคร + auto-login (เก็บ token ทันที)
export async function registerReporterAction(data: RegisterReporterInput): Promise<void> {
  const { result, error } = await registerReporter(data);

  if (error.status === "error") {
    throw new Error(error.errorMessage);
  }

  const userData: User = {
    id: result.data.id,
    email: result.data.email,
    fullName: result.data.fullName,
    phone: result.data.phone,
    role: result.data.role as "REPORTER" | "STAFF",
    isBlocked: result.data.isBlocked,
    createdAt: result.data.createdAt,
  };

  useAuth.getState().setUser(userData);
  useAuth.getState().setToken(result.data.token);
}

// Register Staff — สมัคร + auto-login (เก็บ token ทันที)
export async function registerStaffAction(data: RegisterStaffInput): Promise<void> {
  const { result, error } = await registerStaff(data);

  if (error.status === "error") {
    throw new Error(error.errorMessage);
  }

  const userData: User = {
    id: result.data.id,
    email: result.data.email,
    fullName: result.data.fullName,
    phone: result.data.phone,
    role: result.data.role as "REPORTER" | "STAFF",
    isBlocked: result.data.isBlocked,
    createdAt: result.data.createdAt,
  };

  useAuth.getState().setUser(userData);
  useAuth.getState().setToken(result.data.token);
}
