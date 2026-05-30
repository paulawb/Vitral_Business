"use client";

import { useEffect } from "react";
import { usePathname, useRouter } from "next/navigation";
import { useAuthStore } from "@/store/auth-store";
import { APP_ROUTES } from "@/styles/tokens";
import { hasTenant } from "@/utils/session";

export function useAuthGuard() {
  const router = useRouter();
  const pathname = usePathname();
  const session = useAuthStore((state) => state.session);
  const hydrated = useAuthStore((state) => state.hydrated);

  useEffect(() => {
    if (!hydrated) {
      return;
    }

    if (!session?.accessToken) {
      router.replace(`${APP_ROUTES.login}?redirect=${encodeURIComponent(pathname)}`);
      return;
    }

    if (!hasTenant(session.tenantId) && pathname !== APP_ROUTES.onboarding) {
      router.replace(APP_ROUTES.onboarding);
    }
  }, [hydrated, pathname, router, session]);

  return {
    hydrated,
    authenticated: !!session?.accessToken,
    session
  };
}
