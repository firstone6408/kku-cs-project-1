import { Pressable } from "react-native";
import { Link, Redirect } from "expo-router";
import { useAuth } from "@/store/auth.store";
import RegisterForm from "@/features/auth/components/register-form";
import { Box } from "@/components/ui/box";
import { Text } from "@/components/ui/text";

// หน้าสร้างบัญชี — มี tab สลับ Reporter/Staff อยู่ใน RegisterForm
export default function RegisterScreen() {
  const isLoggedIn = useAuth((state) => !!state.token);

  // ถ้า login แล้ว → redirect กลับหน้าแรก
  if (isLoggedIn) {
    return <Redirect href="/" />;
  }

  return (
    <Box className="flex-1 bg-background-0">
      <RegisterForm />

      {/* ลิงก์ไปหน้า login */}
      <Box className="pb-8 items-center">
        <Link href="/(auth)/login" asChild>
          <Pressable>
            <Text className="text-typography-500">
              มีบัญชีอยู่แล้ว?{" "}
              <Text className="text-primary-600 font-bold">เข้าสู่ระบบ</Text>
            </Text>
          </Pressable>
        </Link>
      </Box>
    </Box>
  );
}
