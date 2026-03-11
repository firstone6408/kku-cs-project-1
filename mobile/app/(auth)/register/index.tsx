import { Link, Redirect } from "expo-router";
import { useAuth } from "@/store/auth.store";
import RegisterForm from "@/features/auth/components/register-form";
import { Box } from "@/components/ui/box";
import { Text } from "@/components/ui/text";
import { Pressable } from "@/components/ui/pressable";
import { KeyboardAvoidingView, Platform } from "react-native";

// หน้าสร้างบัญชี — มี tab สลับ Reporter/Staff อยู่ใน RegisterForm
export default function RegisterScreen() {
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
        <RegisterForm />

        {/* ลิงก์ไปหน้า login */}
        <Box className="pb-8 items-center bg-background-0">
          <Link href="/(auth)/login" asChild>
            <Pressable className="px-4 py-2">
              <Text className="text-typography-500 font-medium">
                มีบัญชีอยู่แล้ว?{" "}
                <Text className="text-primary-600 font-bold ml-1">
                  เข้าสู่ระบบ
                </Text>
              </Text>
            </Pressable>
          </Link>
        </Box>
      </Box>
    </KeyboardAvoidingView>
  );
}
