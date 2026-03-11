import { Stack } from "expo-router";

// Layout สำหรับกลุ่มหน้า auth (login, register)
export default function AuthLayout() {
  return <Stack screenOptions={{ headerShown: false }} />;
}
