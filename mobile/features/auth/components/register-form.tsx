import { useState } from "react";
import { ScrollView } from "react-native";
import { useMutation } from "@tanstack/react-query";
import { UserPlus } from "lucide-react-native";

import {
  registerReporterAction,
  registerStaffAction,
  RegisterReporterFormData,
  RegisterStaffFormData,
} from "@/features/auth/actions/auth.action";
import {
  UserRole,
  StaffRole,
  USER_ROLES,
  STAFF_ROLES,
} from "@/types/auth.type";
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

export default function RegisterForm() {
  const toast = useToast();
  const [activeRole, setActiveRole] = useState<UserRole>(USER_ROLES.REPORTER);
  const [email, setEmail] = useState("");
  const [fullName, setFullName] = useState("");
  const [phone, setPhone] = useState("");
  const [staffRole, setStaffRole] = useState<StaffRole>(STAFF_ROLES.VOLUNTEER);
  const [password, setPassword] = useState("");

  // useMutation — action จัดการ validate + API + store ให้หมด
  const registerMutation = useMutation({
    mutationFn: (data: RegisterReporterFormData | RegisterStaffFormData) =>
      activeRole === USER_ROLES.REPORTER
        ? registerReporterAction(data as RegisterReporterFormData)
        : registerStaffAction(data as RegisterStaffFormData),
    onError: (error: Error) => {
      const isValidation =
        error instanceof AppError && error.type === "validation";
      showToast(toast, {
        title: isValidation ? "กรุณาตรวจสอบข้อมูล" : "สมัครไม่สำเร็จ",
        description: error.message,
        action: isValidation ? "warning" : "error",
      });
    },
  });

  // กดปุ่มสมัคร — ส่ง raw data ไป action (validate อยู่ใน action)
  const handleRegister = () => {
    registerMutation.reset();

    if (activeRole === USER_ROLES.REPORTER) {
      registerMutation.mutate({ email, fullName, phone, password });
    } else {
      registerMutation.mutate({
        email,
        fullName,
        phone,
        role: staffRole,
        password,
      });
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
            <UserPlus size={48} color="#E8711A" strokeWidth={1.5} />
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
              { value: USER_ROLES.REPORTER, label: "ผู้แจ้งเหตุทั่วไป" },
              { value: USER_ROLES.STAFF, label: "พนักงาน / อาสา" },
            ]}
          />
        </Box>

        {/* ฟอร์ม */}
        <FormControl isInvalid={!!registerMutation.error} className="w-full">
          <VStack space="xl">
            <InputField
              label="อีเมล"
              placeholder="email@example.com"
              keyboardType="email-address"
              value={email}
              onChangeText={setEmail}
              isInvalid={!!registerMutation.error && !email}
            />

            <InputField
              label="ชื่อ-สกุล"
              placeholder="ชื่อ-สกุล"
              value={fullName}
              onChangeText={setFullName}
              isInvalid={!!registerMutation.error && !fullName}
              autoCapitalize="words"
            />

            <InputField
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
                    { value: STAFF_ROLES.VOLUNTEER, label: "อาสาสมัคร" },
                    { value: STAFF_ROLES.OFFICER, label: "เจ้าหน้าที่" },
                  ]}
                />
              </VStack>
            )}

            <PasswordField
              label="รหัสผ่าน"
              placeholder="รหัสผ่าน (อย่างน้อย 6 ตัว)"
              value={password}
              onChangeText={setPassword}
              isInvalid={!!registerMutation.error && !password}
            />

            {/* Error */}
            <ErrorMessage
              message="เกิดข้อผิดพลาดในการสมัครสมาชิก"
              visible={registerMutation.isError}
            />

            {/* ปุ่มสมัคร */}
            <SubmitButton
              isPending={registerMutation.isPending}
              onPress={handleRegister}
              className="mt-6"
            >
              {registerMutation.isPending ? "กำลังสร้างบัญชี..." : "สร้างบัญชี"}
            </SubmitButton>
          </VStack>
        </FormControl>
      </VStack>
    </ScrollView>
  );
}
