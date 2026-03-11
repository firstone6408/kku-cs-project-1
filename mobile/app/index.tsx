import { Redirect } from "expo-router";
import { useAuth } from "@/store/auth.store";

// หน้าแรก — เช็คว่า login แล้วหรือยัง แล้ว redirect ไปหน้าที่เหมาะสม
export default function IndexScreen() {
  const token = useAuth((state) => state.token);
  const role = useAuth((state) => state.user?.role);

  // ยังไม่ login → ไปหน้า login
  if (!token) {
    return <Redirect href="/(auth)/login" />;
  }

  // login แล้ว → ไปหน้า home ตาม role
  if (role === "STAFF") {
    return <Redirect href="/(staff)/(tabs)" />;
  }

  return <Redirect href="/(reporter)/(tabs)" />;
}
