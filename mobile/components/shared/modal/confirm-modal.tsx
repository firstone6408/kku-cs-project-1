import {
  AlertDialog,
  AlertDialogBackdrop,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogBody,
  AlertDialogFooter,
} from "@/components/ui/alert-dialog";
import { Heading } from "@/components/ui/heading";
import { Text } from "@/components/ui/text";
import { Button, ButtonText } from "@/components/ui/button";

interface ConfirmModalProps {
  /** เปิด/ปิด modal */
  open: boolean;
  /** callback เปิด/ปิด */
  onOpenChange: (open: boolean) => void;
  /** หัวข้อ */
  title: string;
  /** คำอธิบาย */
  description?: string;
  /** callback เมื่อกดยืนยัน */
  onConfirm: () => void;
  /** ข้อความปุ่มยืนยัน */
  confirmLabel?: string;
  /** ข้อความปุ่มยกเลิก */
  cancelLabel?: string;
  /** สี action ของปุ่มยืนยัน */
  confirmAction?: "primary" | "negative";
}

/**
 * Modal ยืนยันการกระทำ — ใช้ร่วมทั้งระบบ
 *
 * อ้างอิงจาก POS: `components/shared/modal/confirm.tsx`
 *
 * ตัวอย่าง:
 * ```tsx
 * <ConfirmModal
 *   open={showConfirm}
 *   onOpenChange={setShowConfirm}
 *   title="ออกจากระบบ"
 *   description="คุณต้องการออกจากระบบหรือไม่?"
 *   onConfirm={handleLogout}
 * />
 * ```
 */
export function ConfirmModal({
  open,
  onOpenChange,
  title,
  description,
  onConfirm,
  confirmLabel = "ยืนยัน",
  cancelLabel = "ยกเลิก",
  confirmAction = "primary",
}: ConfirmModalProps) {
  return (
    <AlertDialog isOpen={open} onClose={() => onOpenChange(false)}>
      <AlertDialogBackdrop />
      <AlertDialogContent className="rounded-2xl">
        <AlertDialogHeader>
          <Heading size="lg" className="text-typography-900">
            {title}
          </Heading>
        </AlertDialogHeader>
        <AlertDialogBody>
          {description && (
            <Text className="text-typography-500">{description}</Text>
          )}
        </AlertDialogBody>
        <AlertDialogFooter className="gap-3">
          <Button
            variant="outline"
            action="secondary"
            onPress={() => onOpenChange(false)}
            className="flex-1 rounded-xl"
          >
            <ButtonText>{cancelLabel}</ButtonText>
          </Button>
          <Button
            action={confirmAction}
            onPress={() => {
              onConfirm();
              onOpenChange(false);
            }}
            className="flex-1 rounded-xl"
          >
            <ButtonText>{confirmLabel}</ButtonText>
          </Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
