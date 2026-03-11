import { Box } from "@/components/ui/box";
import { Text } from "@/components/ui/text";

interface ErrorMessageProps {
  /** ข้อความ error */
  message: string;
  /** แสดง/ซ่อน */
  visible: boolean;
}

/**
 * แถบแสดง error — ซ่อนเมื่อ visible = false
 *
 * อ้างอิงจาก POS: `components/shared/error/error-message.tsx`
 */
export function ErrorMessage({ message, visible }: ErrorMessageProps) {
  if (!visible) return null;

  return (
    <Box className="bg-error-50 p-3 rounded-lg border border-error-200">
      <Text className="text-error-600 font-medium text-sm text-center">
        {message}
      </Text>
    </Box>
  );
}
