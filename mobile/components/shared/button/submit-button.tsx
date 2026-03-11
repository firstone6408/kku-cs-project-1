import { Button, ButtonText, ButtonSpinner } from "@/components/ui/button";
import { ReactNode } from "react";

interface SubmitButtonProps {
  /** ข้อความปุ่ม */
  children: ReactNode;
  /** กำลังโหลด */
  isPending?: boolean;
  /** ปิดปุ่ม */
  isDisabled?: boolean;
  /** callback เมื่อกด */
  onPress: () => void;
  /** className เพิ่มเติม */
  className?: string;
}

/**
 * ปุ่ม submit — รองรับ loading state (Spinner + disable)
 *
 * อ้างอิงจาก POS: `components/shared/button/submit-button.tsx`
 *
 * ตัวอย่าง:
 * ```tsx
 * <SubmitButton isPending={mutation.isPending} onPress={handleSubmit}>
 *   เข้าสู่ระบบ
 * </SubmitButton>
 * ```
 */
export function SubmitButton({
  children,
  isPending = false,
  isDisabled = false,
  onPress,
  className = "",
}: SubmitButtonProps) {
  return (
    <Button
      size="xl"
      onPress={onPress}
      isDisabled={isPending || isDisabled}
      className={`rounded-2xl shadow-md shadow-primary-500/30 bg-primary-600 active:bg-primary-800 ${className}`}
    >
      {isPending && <ButtonSpinner className="text-white" />}
      <ButtonText className="font-bold text-lg">{children}</ButtonText>
    </Button>
  );
}
