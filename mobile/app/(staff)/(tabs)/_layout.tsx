import { Tabs } from "expo-router";

// Layout สำหรับ Staff tabs
export default function StaffTabsLayout() {
  return (
    <Tabs screenOptions={{ headerShown: false }}>
      <Tabs.Screen name="index" options={{ title: "หน้าหลัก" }} />
    </Tabs>
  );
}
