import { Pressable } from "react-native";
import { Link, Redirect } from "expo-router";
import { useAuth } from "@/store/auth.store";
import LoginForm from "@/features/auth/components/login-form";
import { Box } from "@/components/ui/box";
import { Text } from "@/components/ui/text";

// หน้า Login — มี tab สลับ Reporter/Staff อยู่ใน LoginForm
export default function LoginScreen() {
  const isLoggedIn = useAuth((state) => !!state.token);

  // ถ้า login แล้ว → redirect กลับหน้าแรก
  if (isLoggedIn) {
    return <Redirect href="/" />;
  }

  return (
    <Box className="flex-1 bg-background-0">
      <LoginForm />

      {/* ลิงก์ไปหน้าสมัคร */}
      <Box className="pb-8 items-center">
        <Link href="/(auth)/register" asChild>
          <Pressable>
            <Text className="text-typography-500">
              ยังไม่มีบัญชี?{" "}
              <Text className="text-primary-600 font-bold">สร้างบัญชี</Text>
            </Text>
          </Pressable>
        </Link>
      </Box>
    </Box>
  );
}
