import { useState } from "react";
import { Alert, Pressable } from "react-native";
import { useMutation } from "@tanstack/react-query";
import { EyeIcon, EyeOffIcon } from "lucide-react-native";

import { loginSchema, LoginInput } from "../schemas/auth.schema";
import { loginReporterAction, loginStaffAction } from "../actions/auth.action";

import { Box } from "@/components/ui/box";
import { VStack } from "@/components/ui/vstack";
import { HStack } from "@/components/ui/hstack";
import { Text } from "@/components/ui/text";
import { Heading } from "@/components/ui/heading";
import { Input, InputField, InputIcon, InputSlot } from "@/components/ui/input";
import { Button, ButtonText } from "@/components/ui/button";
import { FormControl } from "@/components/ui/form-control";

// ประเภท role ที่เลือก login
type RoleTab = "REPORTER" | "STAFF";

export default function LoginForm() {
  const [activeRole, setActiveRole] = useState<RoleTab>("REPORTER");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

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
    const parsed = loginSchema.safeParse({ email, password });
    if (!parsed.success) {
      const firstError = parsed.error.issues[0]?.message || "ข้อมูลไม่ถูกต้อง";
      Alert.alert("กรุณาตรวจสอบข้อมูล", firstError);
      return;
    }
    loginMutation.mutate(parsed.data);
  };

  return (
    <Box className="flex-1 justify-center px-6">
      <VStack space="xl">
        {/* หัวข้อ */}
        <VStack className="items-center mb-4">
          <Heading size="2xl" className="text-typography-900">
            เข้าสู่ระบบ
          </Heading>
          <Text size="sm" className="text-typography-500 mt-1">
            เลือกประเภทผู้ใช้แล้วกรอกข้อมูล
          </Text>
        </VStack>

        {/* Tab สลับ Reporter / Staff */}
        <HStack className="bg-background-100 rounded-xl p-1">
          <Pressable
            className={`flex-1 py-3 rounded-lg ${activeRole === "REPORTER" ? "bg-background-0 shadow-sm" : ""}`}
            onPress={() => setActiveRole("REPORTER")}
          >
            <Text
              bold
              className={`text-center ${activeRole === "REPORTER" ? "text-primary-600" : "text-typography-400"}`}
            >
              ผู้แจ้งเหตุ
            </Text>
          </Pressable>
          <Pressable
            className={`flex-1 py-3 rounded-lg ${activeRole === "STAFF" ? "bg-background-0 shadow-sm" : ""}`}
            onPress={() => setActiveRole("STAFF")}
          >
            <Text
              bold
              className={`text-center ${activeRole === "STAFF" ? "text-primary-600" : "text-typography-400"}`}
            >
              พนักงาน
            </Text>
          </Pressable>
        </HStack>

        {/* ฟอร์ม */}
        <FormControl isInvalid={!!loginMutation.error}>
          <VStack space="lg">
            {/* Email */}
            <VStack space="xs">
              <Text className="text-typography-500 font-medium">Email</Text>
              <Input size="xl">
                <InputField
                  placeholder="email@example.com"
                  keyboardType="email-address"
                  autoCapitalize="none"
                  value={email}
                  onChangeText={setEmail}
                />
              </Input>
            </VStack>

            {/* Password */}
            <VStack space="xs">
              <Text className="text-typography-500 font-medium">รหัสผ่าน</Text>
              <Input size="xl">
                <InputField
                  placeholder="รหัสผ่าน"
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChangeText={setPassword}
                />
                <InputSlot
                  className="p-3"
                  onPress={() => setShowPassword(!showPassword)}
                >
                  <InputIcon as={showPassword ? EyeIcon : EyeOffIcon} />
                </InputSlot>
              </Input>
            </VStack>

            {/* ปุ่ม Login */}
            <Button
              size="xl"
              onPress={handleLogin}
              isDisabled={loginMutation.isPending}
              className="rounded-xl mt-2"
            >
              <ButtonText>
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
