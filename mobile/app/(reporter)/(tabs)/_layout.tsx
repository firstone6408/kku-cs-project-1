import { Tabs } from "expo-router";

// Layout สำหรับ Reporter tabs
export default function ReporterTabsLayout() {
  return (
    <Tabs screenOptions={{ headerShown: false }}>
      <Tabs.Screen name="index" options={{ title: "หน้าหลัก" }} />
    </Tabs>
  );
}
