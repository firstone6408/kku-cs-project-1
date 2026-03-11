import { Stack, useRouter } from "expo-router";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { GluestackUIProvider } from "@/components/ui/gluestack-ui-provider";
import "@/global.css";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";
import { useAuth } from "@/store/auth.store";
import { useEffect } from "react";

// สร้าง QueryClient สำหรับ TanStack Query (cache + refetch management)
const queryClient = new QueryClient();

// Root layout — ครอบทั้ง app ด้วย QueryClientProvider
export default function RootLayout() {
  const router = useRouter();
  const isLoggedIn = useAuth((state) => !!state.token);

  useEffect(() => {
    if (!isLoggedIn) {
      router.push("/(auth)/login");
    }
  }, [isLoggedIn, router]);

  return (
    <GluestackUIProvider mode="light">
      <QueryClientProvider client={queryClient}>
        <SafeAreaProvider>
          <SafeAreaView className="flex-1 bg-background-0">
            <Stack screenOptions={{ headerShown: false }} />
          </SafeAreaView>
        </SafeAreaProvider>
      </QueryClientProvider>
    </GluestackUIProvider>
  );
}
