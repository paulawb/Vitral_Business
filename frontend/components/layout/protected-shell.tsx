"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { ReactNode } from "react";
import { usePathname } from "next/navigation";
import { useAuthStore } from "@/store/auth-store";
import { logout as logoutApi } from "@/services/auth-service";
import { APP_ROUTES } from "@/styles/tokens";

const links = [
  { href: APP_ROUTES.dashboard, label: "Dashboard" },
  { href: APP_ROUTES.analytics, label: "Analytics" },
  { href: APP_ROUTES.productsAdmin, label: "Productos" },
  { href: APP_ROUTES.bookingsAdmin, label: "Citas" },
  { href: APP_ROUTES.settings, label: "Negocio" },
  { href: APP_ROUTES.plans, label: "Planes" }
];

export function ProtectedShell({ children }: { children: ReactNode }) {
  const router = useRouter();
  const pathname = usePathname();
  const logout = useAuthStore((state) => state.logout);
  const session = useAuthStore((state) => state.session);

  const handleLogout = async () => {
    await logoutApi();
    logout();
    router.push(APP_ROUTES.home);
  };

  return (
    <div className="min-h-screen bg-sand">
      <div className="container-app grid gap-4 py-4 lg:grid-cols-[260px_1fr]">
        <aside className="panel p-4">
          <div className="mb-6 space-y-1">
            <div className="text-lg font-semibold text-ink">Vitral</div>
            <div className="text-sm text-slate">{session?.user?.correo}</div>
          </div>
          <nav className="grid gap-2">
            {links.map((link) => (
              <Link
                key={link.href}
                href={link.href}
                className={`rounded-md px-3 py-2 text-sm transition ${pathname === link.href ? "bg-ink text-white" : "text-slate hover:bg-slate-50"}`}
              >
                {link.label}
              </Link>
            ))}
          </nav>
          <button onClick={handleLogout} className="mt-6 rounded-md border border-slate-200 px-3 py-2 text-sm text-slate hover:bg-slate-50">
            Cerrar sesion
          </button>
        </aside>
        <main className="space-y-4">{children}</main>
      </div>
    </div>
  );
}
