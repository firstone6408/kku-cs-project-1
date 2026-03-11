import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { ShieldCheck } from "lucide-react-native";

import {
  loginReporterAction,
  loginStaffAction,
  LoginFormData,
} from "@/features/auth/actions/auth.action";
import { UserRole, USER_ROLES } from "@/types/auth.type";
import { AppError } from "@/utils/validate.utils";

import { Box } from "@/components/ui/box";
import { VStack } from "@/components/ui/vstack";
import { Text } from "@/components/ui/text";
import { Heading } from "@/components/ui/heading";
import { FormControl } from "@/components/ui/form-control";

import { InputField } from "@/components/shared/field/input-field";
import { PasswordField } from "@/components/shared/field/password-field";
import { SegmentedControl } from "@/components/shared/tab/segmented-control";
import { SubmitButton } from "@/components/shared/button/submit-button";
import { ErrorMessage } from "@/components/shared/error/error-message";
import { showToast, useToast } from "@/components/shared/toast/show-toast";

export default function LoginForm() {
  const toast = useToast();
  const [activeRole, setActiveRole] = useState<UserRole>(USER_ROLES.REPORTER);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  // useMutation — action จัดการ validate + API + store ให้หมด
  const loginMutation = useMutation({
    mutationFn: (data: LoginFormData) =>
      activeRole === USER_ROLES.REPORTER
        ? loginReporterAction(data)
        : loginStaffAction(data),
    onError: (error: Error) => {
      const isValidation =
        error instanceof AppError && error.type === "validation";
      showToast(toast, {
        title: isValidation ? "กรุณาตรวจสอบข้อมูล" : "เข้าสู่ระบบไม่สำเร็จ",
        description: error.message,
        action: isValidation ? "warning" : "error",
      });
    },
  });

  // กดปุ่ม login — ส่ง raw data ไป action (validate อยู่ใน action)
  const handleLogin = () => {
    loginMutation.reset();
    loginMutation.mutate({ email, password });
  };

  return (
    <Box className="flex-1 justify-center px-6 py-10 bg-background-0">
      <VStack space="2xl">
        {/* หัวข้อและ Logo */}
        <VStack className="items-center mb-6" space="md">
          <Box className="bg-primary-50 p-4 rounded-full mb-2">
            <ShieldCheck size={48} color="#E8711A" strokeWidth={1.5} />
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
              { label: "ผู้แจ้งเหตุทั่วไป", value: USER_ROLES.REPORTER },
              { label: "พนักงาน / อาสา", value: USER_ROLES.STAFF },
            ]}
          />
        </Box>

        {/* ฟอร์ม */}
        <FormControl isInvalid={!!loginMutation.error} className="w-full">
          <VStack space="xl">
            <InputField
              label="อีเมล"
              placeholder="email@example.com"
              keyboardType="email-address"
              value={email}
              onChangeText={setEmail}
              isInvalid={!!loginMutation.error && !email}
            />

            <VStack space="sm">
              <PasswordField
                label="รหัสผ่าน"
                placeholder="กรอกรหัสผ่านของคุณ"
                value={password}
                onChangeText={setPassword}
                isInvalid={!!loginMutation.error && !password}
              />
              <Text className="text-primary-600 text-sm font-semibold text-right mt-1">
                ลืมรหัสผ่าน?
              </Text>
            </VStack>

            {/* Error */}
            <ErrorMessage
              message="อีเมลหรือรหัสผ่านไม่ถูกต้อง"
              visible={loginMutation.isError}
            />

            {/* ปุ่ม Login */}
            <SubmitButton
              isPending={loginMutation.isPending}
              onPress={handleLogin}
              className="mt-6"
            >
              {loginMutation.isPending ? "กำลังเข้าสู่ระบบ..." : "เข้าสู่ระบบ"}
            </SubmitButton>
          </VStack>
        </FormControl>
      </VStack>
    </Box>
  );
}
