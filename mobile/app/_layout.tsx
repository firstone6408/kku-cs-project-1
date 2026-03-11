import { Stack } from "expo-router";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { GluestackUIProvider } from "@/components/ui/gluestack-ui-provider";
import "@/global.css";
import { SafeAreaProvider } from "react-native-safe-area-context";

// สร้าง QueryClient สำหรับ TanStack Query (cache + refetch management)
const queryClient = new QueryClient();

// Root layout — ครอบทั้ง app ด้วย QueryClientProvider
export default function RootLayout() {
  return (
    <GluestackUIProvider mode="light">
      <QueryClientProvider client={queryClient}>
        <SafeAreaProvider>
          <Stack screenOptions={{ headerShown: false }} />
        </SafeAreaProvider>
      </QueryClientProvider>
    </GluestackUIProvider>
  );
}
