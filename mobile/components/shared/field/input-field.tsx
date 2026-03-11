import {
  Input,
  InputField as GluestackInputField,
} from "@/components/ui/input";
import { VStack } from "@/components/ui/vstack";
import { Text } from "@/components/ui/text";

interface InputFieldProps {
  /** ข้อความ label */
  label: string;
  /** placeholder ใน input */
  placeholder?: string;
  /** ค่าที่กรอก */
  value: string;
  /** callback เมื่อค่าเปลี่ยน */
  onChangeText: (text: string) => void;
  /** ประเภท keyboard */
  keyboardType?:
    | "default"
    | "email-address"
    | "numeric"
    | "phone-pad"
    | "number-pad";
  /** รูปแบบ capitalize */
  autoCapitalize?: "none" | "sentences" | "words" | "characters";
  /** แสดง border สีแดง */
  isInvalid?: boolean;
  /** ข้อความ error ใต้ input */
  errorMessage?: string;
}

/**
 * Input field พร้อม label + error — ใช้ร่วมทั้งระบบ
 *
 * อ้างอิงจาก POS: `components/shared/field/input-field.tsx`
 */
export function InputField({
  label,
  placeholder,
  value,
  onChangeText,
  keyboardType = "default",
  autoCapitalize = "none",
  isInvalid = false,
  errorMessage,
}: InputFieldProps) {
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
          keyboardType={keyboardType}
          autoCapitalize={autoCapitalize}
          value={value}
          onChangeText={onChangeText}
          className="text-typography-900"
        />
      </Input>
      {isInvalid && errorMessage && (
        <Text className="text-error-500 text-xs mt-1">{errorMessage}</Text>
      )}
    </VStack>
  );
}
