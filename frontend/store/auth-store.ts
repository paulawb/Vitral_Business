"use client";

import { create } from "zustand";
import { persist } from "zustand/middleware";
import { AuthResponse, SessionState, User } from "@/types/auth";
import { clearSessionStorage, hasTenant, persistSession } from "@/utils/session";

type AuthStore = {
  session: SessionState | null;
  hydrated: boolean;
  setHydrated: (value: boolean) => void;
  setAuth: (payload: AuthResponse) => void;
  updateTenant: (tenantId: string, user?: User) => void;
  logout: () => void;
};

export const useAuthStore = create<AuthStore>()(
  persist(
    (set, get) => ({
      session: null,
      hydrated: false,
      setHydrated: (value) => set({ hydrated: value }),
      setAuth: (payload) => {
        const session: SessionState = {
          accessToken: payload.accessToken,
          refreshToken: payload.refreshToken,
          expiresAt: Date.now() + payload.expiresInSeconds * 1000,
          user: payload.usuario,
          tenantId: payload.tenantId,
          rol: payload.rol
        };
        persistSession(session);
        set({ session });
      },
      updateTenant: (tenantId, user) => {
        const current = get().session;
        if (!current) {
          return;
        }
        const next = {
          ...current,
          tenantId,
          user: user ?? { ...current.user, tenantId }
        };
        persistSession(next);
        set({ session: next });
      },
      logout: () => {
        clearSessionStorage();
        set({ session: null });
      }
    }),
    {
      name: "vitral-auth-store",
      partialize: (state) => ({ session: state.session }),
      onRehydrateStorage: () => (state) => {
        state?.setHydrated(true);
      }
    }
  )
);

export function isAuthenticated() {
  const session = useAuthStore.getState().session;
  return !!session?.accessToken;
}

export function requiresOnboarding() {
  const session = useAuthStore.getState().session;
  return !!session && !hasTenant(session.tenantId);
}
