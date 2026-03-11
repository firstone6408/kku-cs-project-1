import {
  Toast,
  ToastTitle,
  ToastDescription,
  useToast,
} from "@/components/ui/toast";

// ประเภท action ของ toast (กำหนดสี)
type ToastAction = "success" | "error" | "warning" | "info" | "muted";

// ตัวเลือกสำหรับแสดง toast
interface ShowToastOptions {
  title: string;
  description?: string;
  action?: ToastAction;
  duration?: number;
  placement?: "top" | "bottom";
}

/**
 * แสดง toast notification — ใช้แทน Alert.alert()
 *
 * ตัวอย่าง:
 * ```tsx
 * const toast = useToast();
 * showToast(toast, { title: "สำเร็จ", description: "...", action: "success" });
 * ```
 */
export function showToast(
  toast: ReturnType<typeof useToast>,
  options: ShowToastOptions,
) {
  const {
    title,
    description,
    action = "muted",
    duration = 3000,
    placement = "top",
  } = options;

  const id = Math.random().toString();

  toast.show({
    id,
    placement,
    duration,
    render: ({ id: toastId }) => (
      <Toast nativeID={`toast-${toastId}`} action={action} variant="solid">
        <ToastTitle>{title}</ToastTitle>
        {description && <ToastDescription>{description}</ToastDescription>}
      </Toast>
    ),
  });
}

// re-export useToast เพื่อ import จากที่เดียว
export { useToast };
