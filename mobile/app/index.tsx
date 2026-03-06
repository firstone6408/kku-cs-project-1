import { StatusBar } from "expo-status-bar";
import { Text, View } from "react-native";

export default function App() {
  return (
    <View className="flex-1 items-center justify-center">
      <Text className="text-2xl bg-red-500">
        Open up App.tsx to start working on your app!55
      </Text>
      <StatusBar style="auto" />
    </View>
  );
}
