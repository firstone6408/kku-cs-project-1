import {
  Input,
  InputField as GluestackInputField,
  InputIcon,
  InputSlot,
} from "@/components/ui/input";
import { VStack } from "@/components/ui/vstack";
import { Text } from "@/components/ui/text";
import { EyeIcon, EyeOffIcon } from "lucide-react-native";
import { useState } from "react";

interface PasswordFieldProps {
  /** ข้อความ label */
  label?: string;
  /** placeholder */
  placeholder: string;
  /** ค่าที่กรอก */
  value: string;
  /** callback เมื่อค่าเปลี่ยน */
  onChangeText: (value: string) => void;
  /** แสดง border สีแดง */
  isInvalid?: boolean;
  /** ข้อความ error ใต้ input */
  errorMessage?: string;
}

/**
 * Password field พร้อม eye toggle + label + error — ใช้ร่วมทั้งระบบ
 *
 * อ้างอิงจาก POS: `components/shared/field/input-field.tsx` (ปรับเป็น password)
 */
export function PasswordField({
  label = "รหัสผ่าน",
  placeholder,
  value,
  onChangeText,
  isInvalid = false,
  errorMessage,
}: PasswordFieldProps) {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <VStack space="xs">
      <Text className="text-typography-600 font-semibold">{label}</Text>
      <Input
        size="xl"
        variant="outline"
        className={`bg-white rounded-xl ${isInvalid ? "border-error-500" : "border-background-200 focus:border-primary-500"}`}
      >
        <GluestackInputField
          placeholder={placeholder}
          type={showPassword ? "text" : "password"}
          value={value}
          onChangeText={onChangeText}
          className="text-typography-900"
        />
        <InputSlot
          className="pr-4"
          onPress={() => setShowPassword(!showPassword)}
        >
          <InputIcon
            as={showPassword ? EyeIcon : EyeOffIcon}
            className="text-typography-400"
          />
        </InputSlot>
      </Input>
      {isInvalid && errorMessage && (
        <Text className="text-error-500 text-xs mt-1">{errorMessage}</Text>
      )}
    </VStack>
  );
}
