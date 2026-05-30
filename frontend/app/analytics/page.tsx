"use client";
 
import { useEffect } from "react"; 
import { EmptyState } from "@/components/feedback/empty-state";
import { ProtectedShell } from "@/components/layout/protected-shell";
import { Skeleton } from "@/components/ui/skeleton";
import { useAuthGuard } from "@/hooks/use-auth-guard";
import { useDashboardData } from "@/hooks/use-dashboard-data";
import { useDashboardStore } from "@/store/dashboard-store";

export default function AnalyticsPage() {
  const { session } = useAuthGuard();
  const { load } = useDashboardData();
  const { analytics, loading } = useDashboardStore();

  useEffect(() => {
    if (session?.tenantId) {
      void load(session.tenantId);
    }
  }, [load, session?.tenantId]);

  return (
    <ProtectedShell>
      <section className="panel p-6">
        <h1 className="text-2xl font-semibold text-ink">Panel analytics</h1>
      </section>
      {loading ? (
        <Skeleton className="h-72" />
      ) : analytics ? (
        <section className="grid gap-4 lg:grid-cols-2">
          <article className="panel p-6">
            <div className="text-sm text-slate">Productos mas vistos</div>
            <div className="mt-4 space-y-3">
              {analytics.topProducts.length ? analytics.topProducts.map((item, index) => (
                <div key={`${item.name}-${index}`} className="flex items-center justify-between rounded-md bg-slate-50 p-4 text-sm">
                  <span>{String(item.name)}</span>
                  <span>{String(item.views)} vistas</span>
                </div>
              )) : <p className="text-sm text-slate">No hay vistas registradas.</p>}
            </div>
          </article>
          <article className="panel p-6">
            <div className="text-sm text-slate">Horarios pico</div>
            <div className="mt-4 space-y-3">
              {analytics.peakHours.length ? analytics.peakHours.map((item, index) => (
                <div key={`${item.hour}-${index}`} className="flex items-center justify-between rounded-md bg-slate-50 p-4 text-sm">
                  <span>{String(item.hour)}:00</span>
                  <span>{String(item.events)} eventos</span>
                </div>
              )) : <p className="text-sm text-slate">No hay actividad suficiente todavia.</p>}
            </div>
          </article>
        </section>
      ) : (
        <EmptyState title="Analytics vacio" description="Cuando existan eventos reales, esta vista se llenara automaticamente." />
      )}
    </ProtectedShell>
  );
}
