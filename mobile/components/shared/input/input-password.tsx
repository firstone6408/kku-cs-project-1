import { Input, InputField, InputIcon, InputSlot } from "@/components/ui/input";
import { VStack } from "@/components/ui/vstack";
import { Text } from "@/components/ui/text";
import { EyeIcon, EyeOffIcon } from "lucide-react-native";
import { useState } from "react";

interface InputPasswordProps {
  label?: string;
  placeholder: string;
  value: string;
  setValue: (value: string) => void;
  isInvalid?: boolean;
  errorMessage?: string;
}

export default function InputPassword({
  label = "รหัสผ่าน",
  placeholder,
  value,
  setValue,
  isInvalid = false,
  errorMessage,
}: InputPasswordProps) {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <VStack space="xs">
      <Text className="text-typography-600 font-semibold">{label}</Text>
      <Input
        size="xl"
        variant="outline"
        className={`bg-white rounded-xl ${isInvalid ? "border-error-500" : "border-background-200 focus:border-primary-500"}`}
      >
        <InputField
          placeholder={placeholder}
          type={showPassword ? "text" : "password"}
          value={value}
          onChangeText={setValue}
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
