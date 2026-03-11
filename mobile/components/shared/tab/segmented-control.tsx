import { HStack } from "@/components/ui/hstack";
import { Pressable } from "@/components/ui/pressable";
import { Text } from "@/components/ui/text";

type SegmentedControlOption<T extends string> = {
  label: string;
  value: T;
};

type SegmentedControlProps<T extends string> = {
  value: T;
  options: SegmentedControlOption<T>[];
  onChange: (value: T) => void;
};

export function SegmentedControl<T extends string>({
  value,
  options,
  onChange,
}: SegmentedControlProps<T>) {
  return (
    <HStack className="bg-background-100 rounded-xl p-1">
      {options.map((option) => {
        const active = value === option.value;

        return (
          <Pressable
            key={option.value}
            className={`flex-1 py-3 rounded-lg items-center justify-center ${
              active ? "bg-background-0 shadow-sm" : ""
            }`}
            onPress={() => onChange(option.value)}
          >
            <Text
              bold
              className={active ? "text-primary-600" : "text-typography-400"}
            >
              {option.label}
            </Text>
          </Pressable>
        );
      })}
    </HStack>
  );
}
