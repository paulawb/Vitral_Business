"use client";

import { ReactNode, useEffect } from "react";
import { ToastViewport } from "@/components/feedback/toast-viewport";
import { useAuthStore } from "@/store/auth-store";
import { readSession } from "@/utils/session";

export function AppProviders({ children }: { children: ReactNode }) {
  const hydrated = useAuthStore((state) => state.hydrated);
  const setHydrated = useAuthStore((state) => state.setHydrated);

  useEffect(() => {
    if (!hydrated) {
      const session = readSession();
      if (!session) {
        setHydrated(true);
      }
    }
  }, [hydrated, setHydrated]);

  return (
    <>
      {children}
      <ToastViewport />
    </>
  );
}
