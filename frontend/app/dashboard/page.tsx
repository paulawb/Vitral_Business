"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { EmptyState } from "@/components/feedback/empty-state";
import { ProtectedShell } from "@/components/layout/protected-shell";
import { Skeleton } from "@/components/ui/skeleton";
import { Button } from "@/components/ui/button";
import { useAuthGuard } from "@/hooks/use-auth-guard";
import { useDashboardData } from "@/hooks/use-dashboard-data";
import { useDashboardStore } from "@/store/dashboard-store";
import { formatCompactNumber } from "@/utils/format";
import { APP_ROUTES } from "@/styles/tokens";

export default function DashboardPage() {
  const router = useRouter();
  const { hydrated, session } = useAuthGuard();
  const { load } = useDashboardData();
  const { loading, analytics, bookings, products, business } = useDashboardStore();

  useEffect(() => {
    if (session?.tenantId) {
      void load(session.tenantId);
    }
  }, [load, session?.tenantId]);

  if (!hydrated || !session) {
    return <div className="container-app py-8"><Skeleton className="h-80 w-full" /></div>;
  }

  return (
    <ProtectedShell>
      <section className="panel p-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-semibold text-ink">Dashboard administrativo</h1>
            <p className="mt-1 text-sm text-slate">
              {business ? `${business.nombre} · ${business.vertical || "negocio"}` : "Conectando negocio, reservas y conversiones."}
            </p>
          </div>
          <div className="flex gap-2">
            <Button size="sm" onClick={() => router.push(APP_ROUTES.productsAdmin)}>Productos</Button>
            <Button size="sm" onClick={() => router.push(APP_ROUTES.bookingsAdmin)}>Citas</Button>
            <Button size="sm" variant="ghost" onClick={() => router.push(APP_ROUTES.settings)}>Configuracion</Button>
          </div>
        </div>
      </section>
      {loading ? (
        <section className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {[1, 2, 3, 4].map((item) => <Skeleton key={item} className="h-32" />)}
        </section>
      ) : analytics ? (
        <section className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {[
            { label: "Visitas", value: formatCompactNumber(analytics.visits) },
            { label: "Conversiones", value: formatCompactNumber(analytics.conversions) },
            { label: "Clicks WhatsApp", value: formatCompactNumber(analytics.whatsappClicks) },
            { label: "Reservas", value: formatCompactNumber(bookings.length) }
          ].map((stat) => (
            <article key={stat.label} className="panel p-5">
              <div className="text-sm text-slate">{stat.label}</div>
              <div className="mt-2 text-3xl font-semibold text-ink">{stat.value}</div>
            </article>
          ))}
        </section>
      ) : (
        <EmptyState title="Sin datos analiticos aun" description="Las tarjetas se llenaran cuando existan eventos reales de visitas, vistas de producto o clics de WhatsApp." />
      )}
      <section className="grid gap-4 xl:grid-cols-[1.1fr_.9fr]">
        <article className="panel p-6">
          <h2 className="text-lg font-semibold text-ink">Proximas citas</h2>
          <div className="mt-4 space-y-3">
            {bookings.length ? bookings.slice(0, 5).map((booking) => (
              <div key={booking.bookingId} className="rounded-md border border-slate-200 p-4">
                <div className="flex flex-wrap items-center justify-between gap-2">
                  <div>
                    <div className="font-medium text-ink">{booking.customerName}</div>
                    <div className="text-sm text-slate">{booking.serviceName}</div>
                  </div>
                  <div className="text-sm text-slate">{booking.bookingDate} · {booking.startTime}</div>
                </div>
              </div>
            )) : <p className="text-sm text-slate">No hay reservas registradas.</p>}
          </div>
        </article>
        <article className="panel p-6">
          <h2 className="text-lg font-semibold text-ink">Productos activos</h2>
          <div className="mt-4 space-y-3">
            {products.length ? products.slice(0, 5).map((product) => (
              <div key={product.productoId} className="flex items-center justify-between rounded-md bg-slate-50 p-4">
                <div>
                  <div className="font-medium text-ink">{product.nombre}</div>
                  <div className="text-sm text-slate">{product.tipoProducto || "Producto"}</div>
                </div>
                <div className="text-sm font-medium text-ink">{product.precio}</div>
              </div>
            )) : <p className="text-sm text-slate">Aun no existen productos en este tenant.</p>}
          </div>
        </article>
      </section>
    </ProtectedShell>
  );
}
