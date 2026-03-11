import { useAuth } from "@/store/auth.store";
import { useRouter } from "expo-router";

import { Box } from "@/components/ui/box";
import { VStack } from "@/components/ui/vstack";
import { Text } from "@/components/ui/text";
import { Heading } from "@/components/ui/heading";
import { Button, ButtonText } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { GoogleMap } from "@/components/shared/map/google-map";

// หน้าหลัก Reporter — placeholder (จะทำต่อในอนาคต)
export default function ReporterHomeScreen() {
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
      <Button onPress={handleLogout}>
        <ButtonText>Logout</ButtonText>
      </Button>
      <Text className="text-2xl font-bold">Google Map</Text>
      <GoogleMap />
    </Box>
  );
}
