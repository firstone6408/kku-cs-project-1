import { useState } from "react";
import { Alert, ScrollView } from "react-native";
import { useMutation } from "@tanstack/react-query";
import { UserPlus } from "lucide-react-native";

import {
  registerReporterSchema,
  registerStaffSchema,
  RegisterReporterInput,
  RegisterStaffInput,
} from "../schemas/auth.schema";
import {
  registerReporterAction,
  registerStaffAction,
} from "../actions/auth.action";

import { Box } from "@/components/ui/box";
import { VStack } from "@/components/ui/vstack";
import { Text } from "@/components/ui/text";
import { Heading } from "@/components/ui/heading";
import { Button, ButtonText } from "@/components/ui/button";
import { FormControl } from "@/components/ui/form-control";

import FormInput from "@/components/shared/input/form-input";
import InputPassword from "@/components/shared/input/input-password";
import SegmentedControl from "@/components/shared/tab/segmented-control";

// ประเภท role ที่เลือกสมัคร
type RoleTab = "REPORTER" | "STAFF";
type StaffRole = "VOLUNTEER" | "OFFICER";

export default function RegisterForm() {
  const [activeRole, setActiveRole] = useState<RoleTab>("REPORTER");
  const [email, setEmail] = useState("");
  const [fullName, setFullName] = useState("");
  const [phone, setPhone] = useState("");
  const [staffRole, setStaffRole] = useState<StaffRole>("VOLUNTEER");
  const [password, setPassword] = useState("");

  // useMutation สำหรับ register
  const registerMutation = useMutation({
    mutationFn: (data: RegisterReporterInput | RegisterStaffInput) =>
      activeRole === "REPORTER"
        ? registerReporterAction(data as RegisterReporterInput)
        : registerStaffAction(data as RegisterStaffInput),
    onError: (error: Error) => {
      Alert.alert("สมัครไม่สำเร็จ", error.message);
    },
  });

  // กดปุ่มสมัคร
  const handleRegister = () => {
    if (activeRole === "REPORTER") {
      const parsed = registerReporterSchema.safeParse({
        email,
        fullName,
        phone,
        password,
      });
      if (!parsed.success) {
        Alert.alert(
          "กรุณาตรวจสอบข้อมูล",
          parsed.error.issues[0]?.message || "ข้อมูลไม่ถูกต้อง",
        );
        return;
      }
      registerMutation.reset();
      registerMutation.mutate(parsed.data);
    } else {
      const parsed = registerStaffSchema.safeParse({
        email,
        fullName,
        phone,
        role: staffRole,
        password,
      });
      if (!parsed.success) {
        Alert.alert(
          "กรุณาตรวจสอบข้อมูล",
          parsed.error.issues[0]?.message || "ข้อมูลไม่ถูกต้อง",
        );
        return;
      }
      registerMutation.reset();
      registerMutation.mutate(parsed.data);
    }
  };

  return (
    <ScrollView
      className="flex-1 bg-background-0"
      contentContainerClassName="justify-center px-6 py-10"
      showsVerticalScrollIndicator={false}
    >
      <VStack space="2xl">
        {/* หัวข้อและ Logo */}
        <VStack className="items-center mb-4" space="md">
          <Box className="bg-primary-50 p-4 rounded-full mb-2">
            <UserPlus size={48} color="#FF6F00" strokeWidth={1.5} />
          </Box>
          <VStack className="items-center">
            <Heading
              size="3xl"
              className="text-typography-900 font-bold tracking-tight"
            >
              สร้างบัญชีใหม่
            </Heading>
            <Text
              size="md"
              className="text-typography-500 mt-2 text-center px-4 leading-relaxed"
            >
              ร่วมเป็นส่วนหนึ่งในการช่วยเหลือและแจ้งเหตุฉุกเฉิน
            </Text>
          </VStack>
        </VStack>

        {/* Tab สลับ Reporter / Staff */}
        <Box className="shadow-sm rounded-xl mb-2">
          <SegmentedControl
            value={activeRole}
            onChange={setActiveRole}
            options={[
              { value: "REPORTER", label: "ผู้แจ้งเหตุทั่วไป" },
              { value: "STAFF", label: "พนักงาน / อาสา" },
            ]}
          />
        </Box>

        {/* ฟอร์ม */}
        <FormControl isInvalid={!!registerMutation.error} className="w-full">
          <VStack space="xl">
            {/* Email */}
            <FormInput
              label="อีเมล"
              placeholder="email@example.com"
              keyboardType="email-address"
              value={email}
              onChangeText={setEmail}
              isInvalid={!!registerMutation.error && !email}
            />

            {/* ชื่อ-สกุล */}
            <FormInput
              label="ชื่อ-สกุล"
              placeholder="ชื่อ-สกุล"
              value={fullName}
              onChangeText={setFullName}
              isInvalid={!!registerMutation.error && !fullName}
              autoCapitalize="words"
            />

            {/* เบอร์โทร */}
            <FormInput
              label="เบอร์โทรศัพท์"
              placeholder="0812345678"
              keyboardType="phone-pad"
              value={phone}
              onChangeText={setPhone}
              isInvalid={!!registerMutation.error && !phone}
            />

            {/* เลือก role (เฉพาะ Staff) */}
            {activeRole === "STAFF" && (
              <VStack space="xs">
                <Text className="text-typography-600 font-semibold mb-1">
                  บทบาท
                </Text>
                <SegmentedControl
                  value={staffRole}
                  onChange={setStaffRole}
                  options={[
                    { value: "VOLUNTEER", label: "อาสาสมัคร" },
                    { value: "OFFICER", label: "เจ้าหน้าที่" },
                  ]}
                />
              </VStack>
            )}

            {/* Password */}
            <InputPassword
              label="รหัสผ่าน"
              placeholder="รหัสผ่าน (อย่างน้อย 6 ตัว)"
              value={password}
              setValue={setPassword}
              isInvalid={!!registerMutation.error && !password}
            />

            {/* Error Message from Mutation */}
            {registerMutation.isError && (
              <Box className="bg-error-50 p-3 rounded-lg border border-error-200 mt-2">
                <Text className="text-error-600 font-medium text-sm text-center">
                  เกิดข้อผิดพลาดในการสมัครสมาชิก
                </Text>
              </Box>
            )}

            {/* ปุ่มสมัคร */}
            <Button
              size="xl"
              onPress={handleRegister}
              isDisabled={registerMutation.isPending}
              className="rounded-2xl mt-6 shadow-md shadow-primary-500/30 bg-primary-600 hover:bg-primary-700 active:bg-primary-800"
            >
              <ButtonText className="font-bold text-lg">
                {registerMutation.isPending
                  ? "กำลังสร้างบัญชี..."
                  : "สร้างบัญชี"}
              </ButtonText>
            </Button>
          </VStack>
        </FormControl>
      </VStack>
    </ScrollView>
  );
}
