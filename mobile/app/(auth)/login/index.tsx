import { Link, Redirect } from "expo-router";
import { useAuth } from "@/store/auth.store";
import LoginForm from "@/features/auth/components/login-form";
import { Box } from "@/components/ui/box";
import { Text } from "@/components/ui/text";
import { Pressable } from "@/components/ui/pressable";
import { KeyboardAvoidingView, Platform } from "react-native";

// หน้า Login — มี tab สลับ Reporter/Staff อยู่ใน LoginForm
export default function LoginScreen() {
  const isLoggedIn = useAuth((state) => !!state.token);

  // ถ้า login แล้ว → redirect กลับหน้าแรก
  if (isLoggedIn) {
    return <Redirect href="/" />;
  }

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      className="flex-1"
    >
      <Box className="flex-1">
        <LoginForm />

        {/* ลิงก์ไปหน้าสมัคร */}
        <Box className="pb-8 items-center bg-background-0">
          <Link href="/(auth)/register" asChild>
            <Pressable className="px-4 py-2">
              <Text className="text-typography-500 font-medium">
                ยังไม่มีบัญชี?{" "}
                <Text className="text-primary-600 font-bold ml-1">
                  สร้างบัญชีที่นี่
                </Text>
              </Text>
            </Pressable>
          </Link>
        </Box>
      </Box>
    </KeyboardAvoidingView>
  );
}
