import { StyleSheet } from "react-native";
import { Box } from "@/components/ui/box";
import MapView from "react-native-maps";

// Default region — มหาวิทยาลัยขอนแก่น
const KKU_REGION = {
  latitude: 16.4719,
  longitude: 102.8256,
  latitudeDelta: 0.01,
  longitudeDelta: 0.01,
};

interface GoogleMapProps {
  /** ตำแหน่งเริ่มต้นของแผนที่ */
  initialRegion?: typeof KKU_REGION;
  /** className สำหรับ container */
  className?: string;
}

/**
 * Google Map — ใช้ react-native-maps
 *
 * หมายเหตุ: ต้องใช้ development build (ไม่ใช่ Expo Go) เพื่อให้ Google Maps API key ทำงาน
 */
export function GoogleMap({
  initialRegion = KKU_REGION,
  className = "flex-1",
}: GoogleMapProps) {
  return (
    <Box className={className}>
      <MapView
        style={styles.map}
        initialRegion={initialRegion}
        showsUserLocation
        showsMyLocationButton
      />
    </Box>
  );
}

const styles = StyleSheet.create({
  map: {
    width: "100%",
    height: "100%",
  },
});
