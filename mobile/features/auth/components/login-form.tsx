import { useState } from "react";
import { Alert } from "react-native";
import { useMutation } from "@tanstack/react-query";
import { ShieldCheck } from "lucide-react-native";

import { loginSchema, LoginInput } from "@/features/auth/schemas/auth.schema";
import {
  loginReporterAction,
  loginStaffAction,
} from "@/features/auth/actions/auth.action";

import { Box } from "@/components/ui/box";
import { VStack } from "@/components/ui/vstack";
import { Text } from "@/components/ui/text";
import { Heading } from "@/components/ui/heading";
import { Button, ButtonText } from "@/components/ui/button";
import { FormControl } from "@/components/ui/form-control";

import FormInput from "@/components/shared/input/form-input";
import InputPassword from "@/components/shared/input/input-password";
import SegmentedControl from "@/components/shared/tab/segmented-control";

// ประเภท role ที่เลือก login
type RoleTab = "REPORTER" | "STAFF";

export default function LoginForm() {
  const [activeRole, setActiveRole] = useState<RoleTab>("REPORTER");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  // useMutation สำหรับ login
  const loginMutation = useMutation({
    mutationFn: (data: LoginInput) =>
      activeRole === "REPORTER"
        ? loginReporterAction(data)
        : loginStaffAction(data),
    onError: (error: Error) => {
      Alert.alert("เข้าสู่ระบบไม่สำเร็จ", error.message);
    },
  });

  // กดปุ่ม login
  const handleLogin = () => {
    // Validate only when button is pressed
    const parsed = loginSchema.safeParse({ email, password });
    if (!parsed.success) {
      const firstError = parsed.error.issues[0]?.message || "ข้อมูลไม่ถูกต้อง";
      Alert.alert("กรุณาตรวจสอบข้อมูล", firstError);
      return;
    }

    // Clear any previous error before trying again
    loginMutation.reset();
    loginMutation.mutate(parsed.data);
  };

  return (
    <Box className="flex-1 justify-center px-6 py-10 bg-background-0">
      <VStack space="2xl">
        {/* หัวข้อและ Logo */}
        <VStack className="items-center mb-6" space="md">
          <Box className="bg-primary-50 p-4 rounded-full mb-2">
            <ShieldCheck size={48} color="#FF6F00" strokeWidth={1.5} />
          </Box>
          <VStack className="items-center">
            <Heading
              size="3xl"
              className="text-typography-900 font-bold tracking-tight"
            >
              ยินดีต้อนรับกลับ
            </Heading>
            <Text
              size="md"
              className="text-typography-500 mt-2 text-center px-4 leading-relaxed"
            >
              กรุณาเข้าสู่ระบบเพื่อดำเนินการแจ้งเหตุหรือเข้าช่วยเหลือ
            </Text>
          </VStack>
        </VStack>

        {/* Tab สลับ Reporter / Staff */}
        <Box className="shadow-sm rounded-xl mb-2">
          <SegmentedControl
            value={activeRole}
            onChange={setActiveRole}
            options={[
              { label: "ผู้แจ้งเหตุทั่วไป", value: "REPORTER" },
              { label: "พนักงาน / อาสา", value: "STAFF" },
            ]}
          />
        </Box>

        {/* ฟอร์ม */}
        <FormControl isInvalid={!!loginMutation.error} className="w-full">
          <VStack space="xl">
            {/* Email */}
            <FormInput
              label="อีเมล"
              placeholder="email@example.com"
              keyboardType="email-address"
              value={email}
              onChangeText={setEmail}
              isInvalid={!!loginMutation.error && !email}
            />

            {/* Password */}
            <VStack space="sm">
              <InputPassword
                label="รหัสผ่าน"
                placeholder="กรอกรหัสผ่านของคุณ"
                value={password}
                setValue={setPassword}
                isInvalid={!!loginMutation.error && !password}
              />
              <Text className="text-primary-600 text-sm font-semibold text-right mt-1">
                ลืมรหัสผ่าน?
              </Text>
            </VStack>

            {/* Error Message from Mutation */}
            {loginMutation.isError && (
              <Box className="bg-error-50 p-3 rounded-lg border border-error-200 mt-2">
                <Text className="text-error-600 font-medium text-sm text-center">
                  อีเมลหรือรหัสผ่านไม่ถูกต้อง
                </Text>
              </Box>
            )}

            {/* ปุ่ม Login */}
            <Button
              size="xl"
              onPress={handleLogin}
              isDisabled={loginMutation.isPending}
              className="rounded-2xl mt-6 shadow-md shadow-primary-500/30 bg-primary-600 hover:bg-primary-700 active:bg-primary-800"
            >
              <ButtonText className="font-bold text-lg">
                {loginMutation.isPending
                  ? "กำลังเข้าสู่ระบบ..."
                  : "เข้าสู่ระบบ"}
              </ButtonText>
            </Button>
          </VStack>
        </FormControl>
      </VStack>
    </Box>
  );
}
