import { useState } from "react";
import { Alert, Pressable, ScrollView } from "react-native";
import { useMutation } from "@tanstack/react-query";
import { EyeIcon, EyeOffIcon } from "lucide-react-native";

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
import { HStack } from "@/components/ui/hstack";
import { Text } from "@/components/ui/text";
import { Heading } from "@/components/ui/heading";
import { Input, InputField, InputIcon, InputSlot } from "@/components/ui/input";
import { Button, ButtonText } from "@/components/ui/button";
import { FormControl } from "@/components/ui/form-control";

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
  const [showPassword, setShowPassword] = useState(false);

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
      registerMutation.mutate(parsed.data);
    }
  };

  return (
    <ScrollView
      className="flex-1"
      contentContainerClassName="justify-center px-6 py-8"
    >
      <VStack space="xl">
        {/* หัวข้อ */}
        <VStack className="items-center mb-4">
          <Heading size="2xl" className="text-typography-900">
            สร้างบัญชี
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
              className={`text-center font-semibold ${activeRole === "REPORTER" ? "text-primary-600" : "text-typography-400"}`}
            >
              ผู้แจ้งเหตุ
            </Text>
          </Pressable>
          <Pressable
            className={`flex-1 py-3 rounded-lg ${activeRole === "STAFF" ? "bg-background-0 shadow-sm" : ""}`}
            onPress={() => setActiveRole("STAFF")}
          >
            <Text
              className={`text-center font-semibold ${activeRole === "STAFF" ? "text-primary-600" : "text-typography-400"}`}
            >
              พนักงาน
            </Text>
          </Pressable>
        </HStack>

        {/* ฟอร์ม */}
        <FormControl isInvalid={!!registerMutation.error}>
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

            {/* ชื่อ-สกุล */}
            <VStack space="xs">
              <Text className="text-typography-500 font-medium">ชื่อ-สกุล</Text>
              <Input size="xl">
                <InputField
                  placeholder="ชื่อ-สกุล"
                  value={fullName}
                  onChangeText={setFullName}
                />
              </Input>
            </VStack>

            {/* เบอร์โทร */}
            <VStack space="xs">
              <Text className="text-typography-500 font-medium">
                เบอร์โทรศัพท์
              </Text>
              <Input size="xl">
                <InputField
                  placeholder="0812345678"
                  keyboardType="phone-pad"
                  value={phone}
                  onChangeText={setPhone}
                />
              </Input>
            </VStack>

            {/* เลือก role (เฉพาะ Staff) */}
            {activeRole === "STAFF" && (
              <VStack space="xs">
                <Text className="text-typography-500 font-medium">บทบาท</Text>
                <HStack space="md">
                  <Pressable
                    className={`flex-1 py-3 rounded-xl border-2 ${
                      staffRole === "VOLUNTEER"
                        ? "border-primary-500 bg-primary-50"
                        : "border-outline-200 bg-background-0"
                    }`}
                    onPress={() => setStaffRole("VOLUNTEER")}
                  >
                    <Text
                      className={`text-center font-semibold ${
                        staffRole === "VOLUNTEER"
                          ? "text-primary-600"
                          : "text-typography-400"
                      }`}
                    >
                      อาสาสมัคร
                    </Text>
                  </Pressable>
                  <Pressable
                    className={`flex-1 py-3 rounded-xl border-2 ${
                      staffRole === "OFFICER"
                        ? "border-primary-500 bg-primary-50"
                        : "border-outline-200 bg-background-0"
                    }`}
                    onPress={() => setStaffRole("OFFICER")}
                  >
                    <Text
                      className={`text-center font-semibold ${
                        staffRole === "OFFICER"
                          ? "text-primary-600"
                          : "text-typography-400"
                      }`}
                    >
                      เจ้าหน้าที่
                    </Text>
                  </Pressable>
                </HStack>
              </VStack>
            )}

            {/* Password */}
            <VStack space="xs">
              <Text className="text-typography-500 font-medium">รหัสผ่าน</Text>
              <Input size="xl">
                <InputField
                  placeholder="รหัสผ่าน (อย่างน้อย 6 ตัว)"
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChangeText={setPassword}
                />
                <InputSlot
                  className="pr-3"
                  onPress={() => setShowPassword(!showPassword)}
                >
                  <InputIcon as={showPassword ? EyeIcon : EyeOffIcon} />
                </InputSlot>
              </Input>
            </VStack>

            {/* ปุ่มสมัคร */}
            <Button
              size="xl"
              onPress={handleRegister}
              isDisabled={registerMutation.isPending}
              className="rounded-xl mt-2"
            >
              <ButtonText>
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
