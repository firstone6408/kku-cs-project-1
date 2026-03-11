import { useAuth } from "@/store/auth.store";
import { useRouter } from "expo-router";

import { Box } from "@/components/ui/box";
import { VStack } from "@/components/ui/vstack";
import { Text } from "@/components/ui/text";
import { Heading } from "@/components/ui/heading";
import { Button, ButtonText } from "@/components/ui/button";
import { Card } from "@/components/ui/card";

// หน้าหลัก Staff — placeholder (จะทำต่อในอนาคต)
export default function StaffHomeScreen() {
  const user = useAuth((state) => state.user);
  const logout = useAuth((state) => state.logout);
  const router = useRouter();

  // กดออกจากระบบ
  const handleLogout = () => {
    logout();
    router.replace("/");
  };

  return (
    <Box className="flex-1 justify-center items-center bg-background-0 px-6">
      <Card className="w-full p-6 rounded-2xl">
        <VStack space="md" className="items-center">
          {/* Avatar placeholder */}
          <Box className="w-20 h-20 rounded-full bg-success-100 items-center justify-center mb-2">
            <Heading size="2xl" className="text-success-600">
              {user?.fullName?.charAt(0) || "?"}
            </Heading>
          </Box>

          <Heading size="xl" className="text-typography-900">
            สวัสดี, {user?.fullName}
          </Heading>
          <Text size="sm" className="text-typography-500">
            {user?.email}
          </Text>
          <Box className="bg-success-100 px-3 py-1 rounded-full">
            <Text size="xs" className="text-success-700 font-semibold">
              {user?.role === "STAFF" ? "พนักงาน" : user?.role}
            </Text>
          </Box>

          <Button
            action="negative"
            variant="outline"
            size="lg"
            className="mt-6 w-full rounded-xl"
            onPress={handleLogout}
          >
            <ButtonText>ออกจากระบบ</ButtonText>
          </Button>
        </VStack>
      </Card>
    </Box>
  );
}
