import AsyncStorage from "@react-native-async-storage/async-storage";
import { create, StateCreator } from "zustand";
import { createJSONStorage, persist } from "zustand/middleware";

import { User } from "@/types/auth.type";

// state + action ของ auth store
interface AuthState {
  user: User | null;
  token: string | null;

  // actions
  setUser: (user: User) => void;
  setToken: (token: string) => void;
  logout: () => void;
}

// สร้าง store (ยังไม่มี persist)
const authStore: StateCreator<AuthState> = (set) => ({
  user: null,
  token: null,

  setUser: (user: User) => set({ user }),
  setToken: (token: string) => set({ token }),
  logout: () => set({ user: null, token: null }),
});

// เก็บข้อมูลลงเครื่อง (reload app ข้อมูลก็ไม่หาย)
const persistConfig = {
  name: "auth-storage",
  storage: createJSONStorage(() => AsyncStorage),
};

// export hook สำหรับใช้ใน component
const useAuth = create<AuthState>()(persist(authStore, persistConfig));

export { useAuth };
