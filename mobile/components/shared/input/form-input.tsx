import { Input, InputField } from "@/components/ui/input";
import { VStack } from "@/components/ui/vstack";
import { Text } from "@/components/ui/text";

interface FormInputProps {
  label: string;
  placeholder?: string;
  value: string;
  onChangeText: (text: string) => void;
  keyboardType?:
    | "default"
    | "email-address"
    | "numeric"
    | "phone-pad"
    | "number-pad";
  autoCapitalize?: "none" | "sentences" | "words" | "characters";
  isInvalid?: boolean;
  errorMessage?: string;
}

export default function FormInput({
  label,
  placeholder,
  value,
  onChangeText,
  keyboardType = "default",
  autoCapitalize = "none",
  isInvalid = false,
  errorMessage,
}: FormInputProps) {
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
