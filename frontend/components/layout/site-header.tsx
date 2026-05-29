"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { useAuthStore } from "@/store/auth-store";
import { logout as logoutApi } from "@/services/auth-service";
import { APP_ROUTES } from "@/styles/tokens";
import { Button } from "@/components/ui/button";

export function SiteHeader() {
  const [open, setOpen] = useState(false);
  const router = useRouter();
  const session = useAuthStore((state) => state.session);
  const logout = useAuthStore((state) => state.logout);

  const handleLogout = async () => {
    await logoutApi();
    logout();
    router.push(APP_ROUTES.home);
  };

  return (
    <header className="sticky top-0 z-40 border-b border-slate-200 bg-white/95 backdrop-blur-sm">
      <div className="container-app flex items-center justify-between py-4">
        <Link href={APP_ROUTES.home} className="text-lg font-semibold text-ink">
          Vitral
        </Link>
        <button onClick={() => setOpen((value) => !value)} className="rounded-md border border-slate-200 px-3 py-2 text-sm text-slate lg:hidden">
          Menu
        </button>
        <nav className="hidden items-center gap-3 lg:flex">
          <Link href={APP_ROUTES.catalog} className="text-sm text-slate hover:text-ink">Catalogo</Link>
          {session?.accessToken ? (
            <>
              <Link href={APP_ROUTES.dashboard} className="text-sm text-slate hover:text-ink">Panel</Link>
              <Button variant="ghost" onClick={handleLogout}>Salir</Button>
            </>
          ) : (
            <>
              <Link href={APP_ROUTES.login} className="text-sm text-slate hover:text-ink">Login</Link>
              <Button variant="secondary" onClick={() => router.push(APP_ROUTES.register)}>Registro</Button>
            </>
          )}
        </nav>
      </div>
      {open ? (
        <div className="container-app grid gap-3 border-t border-slate-200 py-4 text-slate lg:hidden">
          <Link href={APP_ROUTES.catalog} className="hover:text-ink">Catalogo</Link>
          {session?.accessToken ? <Link href={APP_ROUTES.dashboard} className="hover:text-ink">Panel</Link> : <Link href={APP_ROUTES.login} className="hover:text-ink">Login</Link>}
        </div>
      ) : null}
    </header>
  );
}
